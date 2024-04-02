package com.example.cntsbackend;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.*;
import com.example.cntsbackend.dto.AccountDto;
import com.example.cntsbackend.dto.AccountingRecordDto;
import com.example.cntsbackend.dto.QuotaSaleDto;
import com.example.cntsbackend.dto.TransactionDto;
import com.example.cntsbackend.service.RedisService;
import com.example.cntsbackend.service.serviceimpl.*;
import com.example.cntsbackend.util.AES;
import com.example.cntsbackend.util.GenData;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
    @Autowired
    private GenData genData;

    @Test
    void contextLoads() {
    }
    @Test
    public void sendVerificationCodeByPhoneTest() throws Exception {
        CommonResponse<String> accountCommonResponse = accountService.sendVerificationCodeByPhone("19313028910");
        System.out.println(accountCommonResponse.getMessage());
        System.out.println(accountCommonResponse.getCode());
        System.out.println(accountCommonResponse.getData());
    }
    @Test
    public void sendVerificationCodeByEmailTest() throws Exception {
        CommonResponse<String> accountCommonResponse = accountService.sendVerificationCodeByEmail("1821166153@qq.com");
        System.out.println(accountCommonResponse.getData());
        System.out.println(accountCommonResponse.getCode());
    }
    @Test
    public void VerifyEmailCodeTest() {
        redisServer.setWithExpire("2674314843@qq.com","9717",100);
        CommonResponse<Boolean> stringCommonResponse = accountService.VerifyEmailCode("1821166153@qq.com","6850");
        System.out.println(stringCommonResponse.getMessage());
        System.out.println(stringCommonResponse.getCode());
    }
    @Test
    public void loginByPhoneTest() throws Exception {
        CommonResponse<Map> accountCommonResponse = accountService.loginByPhone("123");
        System.out.println(accountCommonResponse.getData());
        System.out.println(accountCommonResponse.getCode());
    }
    @Test
    public void loginByIDTest() throws Exception {
        CommonResponse<Map> accountCommonResponse = accountService.loginById("12345","123");
        System.out.println(accountCommonResponse.getData());
        System.out.println(accountCommonResponse.getCode());
    }

    @Test
    public void AgreeApplicationTest() throws Exception {
        CommonResponse<String> application = accountService.AgreeApplication(4, 1);
        System.out.println(application.getMessage());
    }
    @Test
    public void RefuseApplicationTest() {
        CommonResponse<String> application = accountService.RefuseApplication(2,1);
        System.out.println(application.getMessage());
    }
    @Test
    public void getPendingReviewAccountTest() throws Exception {
        CommonResponse<List<RegisterApplication>> pendingReviewAccount = accountService.getPendingReviewAccount();
        System.out.println(pendingReviewAccount.getMessage());
        System.out.println(pendingReviewAccount.getData());
    }
    @Test
    public void getAllEnterpriseUsersTest() throws Exception {
        CommonResponse<List<AccountDto>> allEnterpriseUsers = accountService.getAllEnterpriseUsers();
        System.out.println(allEnterpriseUsers.getMessage());
        System.out.println(allEnterpriseUsers.getData());
    }
    @Test
    public void EnterpriseUserRegisterTest() throws Exception {
        CommonResponse<String> application = registerApplicationService.EnterpriseUserRegister(new File("https://www.baidu.com"),"j2ee","888","999",1,1,"123456");
        System.out.println(application.getMessage());
    }
    @Test
    public void changePasswordTest() throws Exception {
        CommonResponse<String> application = accountService.changePassword("1223", "666");
        System.out.println(application.getMessage());
    }
    @Test
    public void changeEmailTest() throws Exception {
        CommonResponse<String> application = accountService.changeEmail("12345", 1);
        System.out.println(application.getMessage());
    }
    @Test
    public void updateAccountInfoTest() {
        CommonResponse<String> stringCommonResponse = accountService.updateAccountInfo(new Account());
        System.out.println(stringCommonResponse.getMessage());
    }
    @Test
    public void AgreeUpdateAccountInfoTest() {
        CommonResponse<String> stringCommonResponse = accountService.AgreeUpdateAccountInfo(1);
        System.out.println(stringCommonResponse.getMessage());
    }
    @Test
    public void VerifyNewPhoneTest() throws Exception {
        CommonResponse<String> stringCommonResponse = accountService.VerifyNewPhone("19313028910");
        System.out.println(stringCommonResponse.getMessage());
        System.out.println(stringCommonResponse.getData());
    }
    @Test
    public void VerifyNewEmailTest() throws Exception {
        CommonResponse<String> stringCommonResponse = accountService.VerifyNewEmail("1821166153@qq.com");
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
        System.out.println(allRemain.getData());
        QuotaSaleDto quotaSaleDto = allRemain.getData().get(0);
        System.out.println(quotaSaleDto.getQuota());
    }

    @Test
    public void getAllCarbonAccountingForReviewTest(){
        CommonResponse<List<AccountingRecordDto>> allCarbonAccountingForReview = accountingRecordService.getAllCarbonAccountingForReview();
        System.out.println(allCarbonAccountingForReview.getData());
        System.out.println(allCarbonAccountingForReview.getData().get(0).getAccount_name());
    }

    @Test
    public void getAllCarbonAccountingTest(){
        CommonResponse<List<AccountingRecordDto>> allCarbonAccountingForReview = accountingRecordService.getAllCarbonAccounting();
        System.out.println(allCarbonAccountingForReview.getData());
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
    @Test
    public void DataAuditorsGetMyCarbonAccountingTest(){
        CommonResponse<List<AccountingRecordDto>> listCommonResponse = accountingRecordService.DataAuditorsGetMyCarbonAccounting(1);
        System.out.println(listCommonResponse.getData());
        System.out.println(listCommonResponse.getData().get(0).getEnterprise_type());
    }
    @Test
    public void ModifyT_limitTest(){
        CommonResponse<String> stringCommonResponse = accountService.ModifyT_limit(13, 600);
        System.out.println(stringCommonResponse.getMessage());
    }
    @Test
    public void GetAllExcessEnterprisesTest() throws Exception {
        CommonResponse<List<AccountDto>> listCommonResponse = accountService.GetAllExcessEnterprises();
        System.out.println(listCommonResponse.getMessage());
        System.out.println(listCommonResponse.getData());
    }

    @Test
    public void checkPhoneNumberExist() throws Exception {
        int commonResponse = accountService.checkPhoneNumberExist("19313028910");
        System.out.println(commonResponse);
    }

    @Test
    public void getPendingReviewAccount() throws Exception {
        CommonResponse<List<RegisterApplication>> pendingReviewAccount = accountService.getPendingReviewAccount();
        System.out.println(pendingReviewAccount.getMessage());
        System.out.println(pendingReviewAccount.getData());
    }


    @Test
    public void GenAccount() throws Exception {
        genData.genAccount(null);
    }

    @Test
    public void GenRegister() throws Exception {
        genData.genRegister();
    }

    @Test
    public void GenAccountWithId() throws Exception {
        genData.genAccount(1);
    }


    @Test
    public void getMyCarbonAccounting(){
        CommonResponse<List<AccountingRecordDto>> myCarbonAccounting = accountingRecordService.getMyCarbonAccounting(1);
        System.out.println(myCarbonAccounting.getData());
    }

    @Test
    public void getEnterpriseInfoByAddress() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("123");
        CommonResponse<List<AccountDto>> enterpriseInfoByAddress = accountService.getEnterpriseInfoByAddress(list);
        System.out.println(enterpriseInfoByAddress.getData());
    }

    //TODO: 解密TEST

    /**
     * 解密 TEST
     * 使用默认密钥 DEFAULT_KEY 只能使用默认密钥
     * @throws Exception 异常
     */
    @Test
    public void decryptTest() throws Exception {
        String context = "123";
        String s2 = AES.decrypt(context);
        System.out.println(s2);
    }

    private static final String DEFAULT_KEY = "2a34575d0f1b7cb39a2c117c0650311a4d3a6e4f507142b45cc3d144bd62ec41";
    //TODO: 根据传入的密钥解密 TEST

    /**
     * 根据传入的密钥解密
     * key为空时使用默认密钥 DEFAULT_KEY 可以使用自定义密钥
     * @throws Exception 异常
     */
    @Test
    public void decryptTest2() throws Exception {
        String context = "123";
        String key = "";
        if (key.isEmpty()) {
            key = DEFAULT_KEY;
        }
        String s2 = AES.decrypt(context, AES.hexToBytes(key));
        System.out.println(s2);
    }


}
