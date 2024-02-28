package com.example.cntsbackend;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.service.serviceimpl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Map;

@SpringBootTest
@MapperScan("com.example.cntsbackend.persistence")
class CntsBackendApplicationTests {
    @Autowired
    private AccountServiceImpl accountService;

    @Test
    void contextLoads() {
    }
    @Test
    public void sendVerificationCodeByPhoneTest() {
        CommonResponse<String> accountCommonResponse = accountService.sendVerificationCodeByPhone("123");
        System.out.println(accountCommonResponse.getMessage());
        System.out.println(accountCommonResponse.getCode());
    }
    @Test
    public void sendVerificationCodeByEmailTest() {
        CommonResponse<String> accountCommonResponse = accountService.sendVerificationCodeByEmail("2674314843@qq.com");
        System.out.println(accountCommonResponse.getData());
        System.out.println(accountCommonResponse.getCode());
    }
    @Test
    public void loginByPhoneTest() {
        CommonResponse<Map> accountCommonResponse = accountService.loginByPhone("123");
        System.out.println(accountCommonResponse.getData());
        System.out.println(accountCommonResponse.getCode());
    }
}
