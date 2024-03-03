package com.example.cntsbackend;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.service.serviceimpl.AccountServiceImpl;
import com.example.cntsbackend.service.serviceimpl.RegisterApplicationServiceImpl;
import com.example.cntsbackend.service.serviceimpl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Date;
import java.util.Map;

@SpringBootTest
@MapperScan("com.example.cntsbackend.persistence")
class CntsBackendApplicationTests {
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private TransactionServiceImpl transactionService;
    @Autowired
    private RegisterApplicationServiceImpl registerApplicationService;

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
    @Test
    public void PublishTransactionTest() {
        CommonResponse<String> transaction = transactionService.PublishTransaction("aaa", 200);
        System.out.println(transaction.getMessage());
    }

    @Test
    public void CompleteTransactionTest() {
        CommonResponse<String> transaction = transactionService.CompleteTransaction("aaa", 1);
        System.out.println(transaction.getMessage());
    }
    @Test
    public void AgreeApplicationTest() {
        CommonResponse<String> application = accountService.AgreeApplication("bbb", "123");
        System.out.println(application.getMessage());
    }
    @Test
    public void EnterpriseUserRegisterTest() {
        CommonResponse<String> application = registerApplicationService.EnterpriseUserRegister(new File("https://www.baidu.com"),"111","111","111","111",1,0,"111");
        System.out.println(application.getMessage());
    }
    @Test
    public void changePasswordTest() {
        CommonResponse<String> application = accountService.changePassword("1223", "666");
        System.out.println(application.getMessage());
    }
    @Test
    public void updateAccountInfoTest() {
        CommonResponse<String> stringCommonResponse = accountService.updateAccountInfo(new Account());
        System.out.println(stringCommonResponse.getMessage());
    }
    @Test
    public void AgreeUpdateAccountInfoTest() {
        CommonResponse<String> stringCommonResponse = accountService.AgreeUpdateAccountInfo("666", "666");
        System.out.println(stringCommonResponse.getMessage());
    }

}
