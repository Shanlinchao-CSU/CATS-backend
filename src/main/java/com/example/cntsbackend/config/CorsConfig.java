package com.example.cntsbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true); // 允许发送cookie
        config.addAllowedOrigin("*"); // 允许所有源
        config.addAllowedHeader("*"); // 允许所有头部信息
        config.addAllowedMethod("*"); // 允许所有HTTP方法
        source.registerCorsConfiguration("/**", config); // 对接口配置跨域设置
        return new CorsFilter(source);
    }
}
