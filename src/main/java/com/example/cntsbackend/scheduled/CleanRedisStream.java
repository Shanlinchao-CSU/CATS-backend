package com.example.cntsbackend.scheduled;

import com.example.cntsbackend.config.RedisStreamConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanRedisStream {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisStreamConfig redisStreamConfig;
    @Value("${redis.stream.clear.num}")
    private long clear_num;


    /**
     * 定时清理stream中的数据
     * 保留 clear_num 条
     * 但是该方法会一起清除未处理的消息 -- 由于短信验证码的时效性，所以任务每分钟执行一次
     */

    @Scheduled(cron="0/60 * * * * ?")
    public void reportCurrentTime() {
        // 定时的清理stream中的数据，保留 clear_num 条 （配置文件中配置）
        this.stringRedisTemplate.opsForStream().trim(redisStreamConfig.getStream(),clear_num);
    }
}
