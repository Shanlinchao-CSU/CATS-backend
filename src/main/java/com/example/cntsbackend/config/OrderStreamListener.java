package com.example.cntsbackend.config;


import com.example.cntsbackend.service.RedisService;
import com.example.cntsbackend.util.SendMailUtil;
import com.example.cntsbackend.util.SendPhoneUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;


@Component
public class OrderStreamListener implements StreamListener<String, ObjectRecord<String, String>> {
    static final Logger LOGGER = LoggerFactory.getLogger(OrderStreamListener.class);
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisStreamConfig redisStreamConfig;

    @Autowired
    RedisService redisService;

    @Override
    public void onMessage(ObjectRecord<String, String> message) {
        try{
            // 消息ID
            RecordId messageId = message.getId();

            // 消息的key和value
            String string = message.getValue();
//            解析消息
            String[] values = string.split("\\+");

            switch (values[0]){
                case "e":
                    String email = values[1];
                    String verificationCode = values[2];
                    SendMailUtil.sendQQEmail(email, Integer.parseInt(verificationCode));
                    boolean b = redisService.hasKey(email);
                    if(b){
                        redisService.delete(email);
                    }
                    redisService.setWithExpire(email,verificationCode,600);
                    break;
                case "p":
                    String phoneNumber = values[1];
                    String verificationCode1 = values[2];
                    SendPhoneUtil.sendSMS(phoneNumber,verificationCode1);
                    boolean bl = redisService.hasKey(phoneNumber);
                    if(bl){
                        redisService.delete(phoneNumber);
                    }
                    redisService.setWithExpire(phoneNumber,verificationCode1,600);
            }


            LOGGER.info("StreamMessageListener  stream message。messageId={}, stream={}, body={}", messageId, message.getStream(), string);
            // 通过RedisTemplate手动确认消息
            this.stringRedisTemplate.opsForStream().acknowledge(redisStreamConfig.getGroup(), message);
            // 从消息队列中移除
            this.stringRedisTemplate.opsForStream().delete(redisStreamConfig.getStream(), messageId);
        }catch (Exception e){
            // 处理异常
            LOGGER.error("StreamMessageListener  stream message error");
        }

    }
}
