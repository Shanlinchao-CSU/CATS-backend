package com.example.cntsbackend.service;

import java.util.Map;

public interface RedisService {
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
    //存入Token
    void setToken(String key, Object value);
    //获取Token
    Object getToken(String key);
    //删除Token
    void deleteToken(String key);
    //判断Token是否存在
    boolean hasToken(String key);
    //Token续期
    boolean renewToken(String key);
    // XADD
    void XAdd(String value);

}
