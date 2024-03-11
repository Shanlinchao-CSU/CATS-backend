package com.example.cntsbackend.service;

public interface RedisServer {
    //设置key-value
    void set(String key, Object value);
    //获取key对应的value
    Object get(String key);
    //删除key-value
    void delete(String key);
    //判断key是否存在
    boolean hasKey(String key);
    //设置key-value并设置过期时间
    void setWithExpire(String key, Object value, long expire);
}
