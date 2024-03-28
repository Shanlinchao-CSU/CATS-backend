package com.example.cntsbackend.service.serviceimpl;

import com.example.cntsbackend.config.RedisStreamConfig;
import com.example.cntsbackend.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisStreamConfig redisStreamConfig;

    @Value("${redis.expire.time}")
    private int expireTime;

    /**
     * 设置key-value
     *
     * @param key  键
     * @param value  值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取key对应的value
     *
     * @param key  键
     * @return  值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除key-value
     *
     * @param key  键
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 判断key是否存在
     *
     * @param key  键
     * @return  是否存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 设置key-value并设置过期时间
     *
     * @param key  键
     */
    public void setWithExpire(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 设置key-value并设置过期时间  - 给已有的key设置过期时间
     *
     * @param key  键
     */
    public void setExpire(String key, long expire) {
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }


    /**
     * 存入Token
     *
     * @param key  键
     * @param value  值
     */
    public void setToken(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 获取Token
     *
     * @param key  键
     * @return  值
     */
    public Object getToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除Token
     *
     * @param key  键
     */
    public void deleteToken(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 判断Token是否存在
     *
     * @param key  键
     * @return  是否存在
     */
    public boolean hasToken(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * Token续期
     *
     * @param key  键
     * @return  是否成功
     */
    public boolean renewToken(String key) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, expireTime, TimeUnit.SECONDS));
    }

    /**
     * XADD 添加消息到stream中
     *
     * @param value 消息内容 格式：number + code 例(e/q+111@qq.com+1234)
     */
    public void XAdd(String value) {
        StringRecord record = StreamRecords.string(Collections.singletonMap("info", value)).withStreamKey(redisStreamConfig.getStream());
        stringRedisTemplate.opsForStream().add(record);
    }


}
