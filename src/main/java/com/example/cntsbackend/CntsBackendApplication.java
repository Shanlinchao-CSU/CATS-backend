package com.example.cntsbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.example.cntsbackend.persistence")
@EnableScheduling
public class CntsBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CntsBackendApplication.class, args);
    }
}
