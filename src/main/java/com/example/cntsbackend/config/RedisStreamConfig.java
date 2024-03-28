package com.example.cntsbackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "redis.stream")
public class RedisStreamConfig {
    private String stream;
    private String stream2;
    private String group;
    private String consumer;
    private String consumer2;
    private String del;
}
