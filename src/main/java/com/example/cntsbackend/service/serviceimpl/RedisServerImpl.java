package com.example.cntsbackend.service.serviceimpl;

import com.example.cntsbackend.service.RedisServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServerImpl implements RedisServer {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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

}
