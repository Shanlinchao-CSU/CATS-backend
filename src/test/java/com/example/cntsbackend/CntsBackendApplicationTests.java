package com.example.cntsbackend;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.*;
import com.example.cntsbackend.dto.AccountingRecordDto;
import com.example.cntsbackend.dto.QuotaSaleDto;
import com.example.cntsbackend.dto.TransactionDto;
import com.example.cntsbackend.service.serviceimpl.*;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
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
    @Autowired
    private QuotaSaleServiceImpl quotaSaleService;
    @Autowired
    private AccountingRecordServiceImpl accountingRecordService;
    @Autowired
    private RedisServiceImpl redisServer;

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
    public void VerifyEmailCodeTest() {
        CommonResponse<String> stringCommonResponse = accountService.VerifyEmailCode("2674314843@qq.com","9717");
        System.out.println(stringCommonResponse.getMessage());
        System.out.println(stringCommonResponse.getCode());
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
    public void AgreeApplicationTest() {
        CommonResponse<String> application = accountService.AgreeApplication(2, 1);
        System.out.println(application.getMessage());
    }
    @Test
    public void RefuseApplicationTest() {
        CommonResponse<String> application = accountService.RefuseApplication(2,1);
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
        CommonResponse<String> application = registerApplicationService.EnterpriseUserRegister(new File("https://www.baidu.com"),"zs","111","111",1,1);
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
    public void PublishTransactionTest() {
        CommonResponse<String> transaction = quotaSaleService.PublishTransaction(1, 200,200);
        System.out.println(transaction.getMessage());
    }

    @Test
    public void CompleteTransactionTest() {
        CommonResponse<String> stringCommonResponse = transactionService.CompleteTransaction(6, 2, 100);
        System.out.println(stringCommonResponse.getMessage());
    }
    @Test
    public void getAllTransactionDatasTest(){
        CommonResponse<List<TransactionDto>> allTransactionDatas = transactionService.getAllTransactionDatas();
        System.out.println(allTransactionDatas.getData());
        TransactionDto transactionDto = allTransactionDatas.getData().get(0);
        System.out.println(transactionDto.getBuyer_account_name());
    }

    @Test
    public void getMyFinishedTransactionDatasTest(){
        CommonResponse<List<TransactionDto>> myFinishedTransactionDatas = transactionService.getMyFinishedTransactionDatas(1);
        System.out.println(myFinishedTransactionDatas.getData());
    }

    @Test
    public void cancelTransactionDataTest(){
        CommonResponse<String> stringCommonResponse = quotaSaleService.cancelTransactionData(1);
        System.out.println(stringCommonResponse.getMessage());
    }
    @Test
    public void ModifyUnitPriceTest(){
        CommonResponse<String> stringCommonResponse = quotaSaleService.ModifyUnitPrice(2,100);
        System.out.println(stringCommonResponse.getMessage());
    }
    @Test
    public void getRemainTest(){
        CommonResponse<List<QuotaSale>> remain = quotaSaleService.getRemain(1);
        System.out.println(remain.getData());
        System.out.println(remain.getData().get(0).toString());
    }
    @Test
    public void getAllRemainTest(){
        CommonResponse<List<QuotaSaleDto>> allRemain = quotaSaleService.getAllRemain();
        System.out.println(allRemain.getMessage());
        QuotaSaleDto quotaSaleDto = allRemain.getData().get(0);
        System.out.println(quotaSaleDto.getQuota());
    }

    @Test
    public void getAllCarbonAccountingForReviewTest(){
        CommonResponse<List<AccountingRecordDto>> allCarbonAccountingForReview = accountingRecordService.getAllCarbonAccountingForReview();
        System.out.println(allCarbonAccountingForReview.getData().get(0));
        System.out.println(allCarbonAccountingForReview.getData().get(0).getAccount_name());
    }

    @Test
    public void getAllCarbonAccountingTest(){
        CommonResponse<List<AccountingRecordDto>> allCarbonAccountingForReview = accountingRecordService.getAllCarbonAccounting();
        System.out.println(allCarbonAccountingForReview.getData().get(0));
        System.out.println(allCarbonAccountingForReview.getData().get(0).getEnterprise_type());
    }
    @Test
    public void CancelMyCarbonAccountingTest(){
        CommonResponse<String> stringCommonResponse = accountingRecordService.CancelMyCarbonAccounting(1);
        System.out.println(stringCommonResponse.getMessage());
    }
    @Test
    public void ModifyMyCarbonAccountingTest(){
        CommonResponse<String> stringCommonResponse = accountingRecordService.ModifyMyCarbonAccounting(1, new AccountingRecord("111","111","111"));
        System.out.println(stringCommonResponse.getMessage());
    }

    @Test
    public void setTest(){
        redisServer.set("test","test");
    }

    @Test
    public void getTest(){
        Object test = redisServer.get("test");
        System.out.println(test);
    }

    @Test
    public void deleteTest(){
        redisServer.delete("test");
    }

    @Test
    public void hasKeyTest(){
        boolean test = redisServer.hasKey("test");
        System.out.println(test);
    }

    @Test
    public void setWithExpireTest(){
        redisServer.setWithExpire("test","test",100);
    }

    @Test
    public void setExpireTest(){
        redisServer.setExpire("test",10);
    }

}
