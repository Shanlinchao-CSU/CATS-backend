package com.example.cntsbackend;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.RegisterApplication;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.service.serviceimpl.AccountServiceImpl;
import com.example.cntsbackend.service.serviceimpl.RegisterApplicationServiceImpl;
import com.example.cntsbackend.service.serviceimpl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Date;
import java.util.List;
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
        CommonResponse<String> accountCommonResponse = accountService.sendVerificationCodeByPhone("19313028910");
        System.out.println(accountCommonResponse.getMessage());
        System.out.println(accountCommonResponse.getCode());
        System.out.println(accountCommonResponse.getData());
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
    public void loginByIDTest() {
        CommonResponse<Map> accountCommonResponse = accountService.loginById("123","123");
        System.out.println(accountCommonResponse.getData());
        System.out.println(accountCommonResponse.getCode());
    }
    @Test
    public void PublishTransactionTest() {
//        CommonResponse<String> transaction = transactionService.PublishTransaction("aaa", 200,200);
//        System.out.println(transaction.getMessage());
    }

    @Test
    public void CompleteTransactionTest() {
//        CommonResponse<String> transaction = transactionService.CompleteTransaction("bbb", 2);
//        System.out.println(transaction.getMessage());
    }
    @Test
    public void AgreeApplicationTest() {
        CommonResponse<String> application = accountService.AgreeApplication("111", "111",1,"2");
        System.out.println(application.getMessage());
    }
    @Test
    public void RefuseApplicationTest() {
        CommonResponse<String> application = accountService.RefuseApplication("111", "111",1);
        System.out.println(application.getMessage());
    }
    @Test
    public void getPendingReviewAccountTest() {
        CommonResponse<List<RegisterApplication>> pendingReviewAccount = accountService.getPendingReviewAccount();
        System.out.println(pendingReviewAccount.getMessage());
        System.out.println(pendingReviewAccount.getData());
    }
    @Test
    public void EnterpriseUserRegisterTest() {
        CommonResponse<String> application = registerApplicationService.EnterpriseUserRegister(new File("https://www.baidu.com"),"zs","111","111","111",1,1);
        System.out.println(application.getMessage());
    }
    @Test
    public void changePasswordTest() {
        CommonResponse<String> application = accountService.changePassword("1223", "666");
        System.out.println(application.getMessage());
    }
    @Test
    public void changeEmailTest() {
        CommonResponse<String> application = accountService.changeEmail("123", "2674314843@qq.com");
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
    @Test
    public void VerifyNewPhoneTest() {
        CommonResponse<String> stringCommonResponse = accountService.VerifyNewPhone("19313028910");
        System.out.println(stringCommonResponse.getMessage());
        System.out.println(stringCommonResponse.getData());
    }
    @Test
    public void VerifyNewEmailTest() {
        CommonResponse<String> stringCommonResponse = accountService.VerifyNewEmail("2674314843@qq.com");
        System.out.println(stringCommonResponse.getMessage());
        System.out.println(stringCommonResponse.getData());
    }
    @Test
    public void getAllUnfinishTransactionDatasTest(){
        CommonResponse<List<Transaction>> allUnfinishTransactionDatas = transactionService.getAllUnfinishTransactionDatas();
        System.out.println(allUnfinishTransactionDatas.getData());
    }

    @Test
    public void getMyFinishedTransactionDatasTest(){
        CommonResponse<List<Transaction>> allUnfinishTransactionDatas = transactionService.getMyFinishedTransactionDatas("aaa");
        System.out.println(allUnfinishTransactionDatas.getData());
    }

    @Test
    public void cancelTransactionDataTest(){
//        CommonResponse<String> stringCommonResponse = transactionService.cancelTransactionData(1);
//        System.out.println(stringCommonResponse.getMessage());
    }

}
