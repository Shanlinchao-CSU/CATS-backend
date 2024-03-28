package com.example.cntsbackend.scheduled;

import com.example.cntsbackend.config.RedisStreamConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.redis.connection.RedisStreamCommands.XClaimOptions.minIdle;

@Component
public class RedisStreamScheduled {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisStreamScheduled.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisStreamConfig redisStreamConfig;

    /**
     * 每隔5秒钟，扫描一下有没有等待自己消费的
     * 处理死信队列，如果发送给消费者1超过1分钟还没有ack，则转发给消费者2，如果超过20秒，并且转发次数为2，进行手动ack。并且记录异常信息
     */
    @Scheduled(cron="0/5 * * * * ?")
    public void scanPendingMsg() {
        StreamOperations<String, String, String> streamOperations = this.stringRedisTemplate.opsForStream();
        // 获取group中的pending消息信息，本质上就是执行XPENDING指令
        PendingMessagesSummary pendingMessagesSummary = streamOperations.pending(redisStreamConfig.getStream(), redisStreamConfig.getGroup());
        // 所有pending消息的数量
        long totalPendingMessages = 0;
        if (pendingMessagesSummary != null) {
            totalPendingMessages = pendingMessagesSummary.getTotalPendingMessages();
        }
        if(totalPendingMessages == 0){
            return;
        }
        // 消费组名称
        String groupName= pendingMessagesSummary.getGroupName();
        // pending队列中的最小ID
        String minMessageId = pendingMessagesSummary.minMessageId();
        // pending队列中的最大ID
        String maxMessageId = pendingMessagesSummary.maxMessageId();
        LOGGER.info("流：{},消费组：{}，一共有{}条pending消息，最大ID={}，最小ID={}", redisStreamConfig.getStream(),groupName, totalPendingMessages, minMessageId, maxMessageId);
        // 获取每个消费者的pending消息数量
        Map<String, Long> pendingMessagesPerConsumer = pendingMessagesSummary.getPendingMessagesPerConsumer();
        Map<String, List<RecordId>> consumerRecordIdMap = new HashMap<>();
        // 遍历每个消费者中的pending消息
        pendingMessagesPerConsumer.forEach((consumer, value) -> {
            // 待转组的 RecordId
            List<RecordId> list = new ArrayList<>();
            // 消费者 consumer
            // 消费者的pending消息数量
            long consumerTotalPendingMessages = value;
            LOGGER.info("消费者：{}，一共有{}条pending消息", consumer, consumerTotalPendingMessages);
            if (consumerTotalPendingMessages > 0) {
                // 读取消费者pending队列的前10条记录，从ID=0的记录开始，一直到ID最大值
                PendingMessages pendingMessages = streamOperations.pending(redisStreamConfig.getStream(), Consumer.from(redisStreamConfig.getGroup(), consumer), Range.closed("0", "+"), 10);
                // 遍历所有pending消息的详情
                pendingMessages.forEach(message -> {
                    // 消息的ID
                    RecordId recordId = message.getId();
                    // 消息从消费组中获取，到此刻的时间
                    Duration elapsedTimeSinceLastDelivery = message.getElapsedTimeSinceLastDelivery();
                    // 消息被获取的次数
                    long deliveryCount = message.getTotalDeliveryCount();
                    // 判断是否超过60秒没有消费
                    if (elapsedTimeSinceLastDelivery.getSeconds() > 20) {
                        // 如果消息被消费的次数为1，则进行一次转组，否则手动消费
                        if (1 == deliveryCount) {
                            list.add(recordId);
                        } else {
                            LOGGER.info("手动ACK消息,并记录异常，id={}, elapsedTimeSinceLastDelivery={}, deliveryCount={}", recordId, elapsedTimeSinceLastDelivery, deliveryCount);
                            streamOperations.acknowledge(redisStreamConfig.getStream(), redisStreamConfig.getGroup(), recordId);
                            streamOperations.delete(redisStreamConfig.getStream(), recordId);
                        }
                    }
                });
                if (!list.isEmpty()) {
                    consumerRecordIdMap.put(consumer, list);
                }
            }
        });
        // 最后将待转组的消息进行转组
        if(!consumerRecordIdMap.isEmpty()){
            this.changeConsumer(consumerRecordIdMap);
        }

    }

    /**
     * 将消息进行转组
     * @param consumerRecordIdMap 待转组的消息
     */
    private void changeConsumer(Map<String,List<RecordId>> consumerRecordIdMap) {
        consumerRecordIdMap.forEach((oldComsumer, recordIds) -> {
            // 根据当前consumer去获取另外一个consumer
            String newConsumer = redisStreamConfig.getConsumers().stream().filter(s -> !s.equals(oldComsumer)).toList().get(0);
            List<ByteRecord> retVal = this.stringRedisTemplate.execute((RedisCallback<List<ByteRecord>>) redisConnection -> {
                // 相当于执行XCLAIM操作，批量将某一个consumer中的消息转到另外一个consumer中
                return redisConnection.streamCommands().xClaim(redisStreamConfig.getStream().getBytes(),
                        redisStreamConfig.getGroup(), newConsumer, minIdle(Duration.ofSeconds(10)).ids(recordIds));
            });
            if (retVal != null) {
                for (ByteRecord byteRecord : retVal) {
                    LOGGER.info("更改消息的消费者：id={}, value={},newConsumer={}", byteRecord.getId(), byteRecord.getValue(), newConsumer);
                }
            }else {
                LOGGER.error("更改消息的消费者失败");
            }
        });
    }



}
