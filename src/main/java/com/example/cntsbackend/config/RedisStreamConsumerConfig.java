package com.example.cntsbackend.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


import java.time.Duration;

@Configuration
public class RedisStreamConsumerConfig {

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    RedisStreamConfig redisStreamConfig;

    /**
     * 主要做的是将OrderStreamListener监听绑定消费者，用于接收消息
     *
     * @param connectionFactory 连接工厂
     * @param streamListener    消费者监听器
     * @return  StreamMessageListenerContainer
     */
    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> consumerListener(
            RedisConnectionFactory connectionFactory,
            OrderStreamListener streamListener) {

        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container =
                streamContainer(redisStreamConfig.getStream(), connectionFactory, streamListener);
        container.start();
        return container;
    }

    /**
     * 主要做的是将OrderStreamListener监听绑定消费者，用于接收消息
     *
     * @param connectionFactory 连接工厂
     * @param streamListener    消费者监听器
     * @return  StreamMessageListenerContainer
     */
    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> consumer2Listener(
            RedisConnectionFactory connectionFactory,
            OrderStreamListener streamListener) {
        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container =
                streamContainer(redisStreamConfig.getStream2(), connectionFactory, streamListener);
        container.start();
        return container;
    }

    /**
     * @param mystream          从哪个流接收数据
     * @param connectionFactory 连接工厂
     * @param streamListener    绑定的监听类
     * @return  StreamMessageListenerContainer
     */
    private StreamMessageListenerContainer<String, ObjectRecord<String, String>> streamContainer(
            String mystream,
            RedisConnectionFactory connectionFactory,
            StreamListener<String, ObjectRecord<String, String>> streamListener) {

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        .pollTimeout(Duration.ofSeconds(5)) // 拉取消息超时时间
                        .batchSize(10) // 批量抓取消息
                        .targetType(String.class) // 传递的数据类型
                        .executor(threadPoolTaskExecutor)
                        .build();
        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container = StreamMessageListenerContainer
                .create(connectionFactory, options);
        //指定消费最新的消息
//        StreamOffset<String> offset = StreamOffset.create(mystream, ReadOffset.latest());
        StreamOffset<String> offset = StreamOffset.create(mystream, ReadOffset.from("0-0"));
        //创建消费者
        Consumer consumer = Consumer.from(redisStreamConfig.getGroup(), redisStreamConfig.getConsumer());
        StreamMessageListenerContainer.StreamReadRequest<String> streamReadRequest =
                StreamMessageListenerContainer.StreamReadRequest.builder(offset)
                .errorHandler((error) -> {
                })
                .cancelOnError(e -> false)
                .consumer(consumer)
                //关闭自动ack确认
                .autoAcknowledge(false)
                .build();
        //指定消费者对象
        container.register(streamReadRequest, streamListener);
        return container;
    }

}
