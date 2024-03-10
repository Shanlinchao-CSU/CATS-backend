package com.example.cntsbackend.controller;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.AccountingRecord;
import com.example.cntsbackend.domain.RegisterApplication;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.dto.TransactionDto;
import com.example.cntsbackend.service.AccountService;
import com.example.cntsbackend.service.AccountingRecordService;
import com.example.cntsbackend.service.TransactionService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Administrator 管理员controller
 */
@RestController
public class AdministratorController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountingRecordService accountingRecordService;

    /**
     * 第三方监管机构和管理员查看交易信息
     *
     * @return 交易信息 List<Transaction>
     */
    @GetMapping("/administrator/transaction")
    public CommonResponse<List<TransactionDto>> getAllTransactionDatas() {
        return transactionService.getAllTransactionDatas();
    }

    /**
     * 获取所有待审核的碳核算请求
     *
     * @return 待审核的碳核算请求 List<AccountingRecord>
     */
    @GetMapping("/administrator/accounting_record/review")
    public CommonResponse<List<AccountingRecord>> getAllCarbonAccountingForReview() {
        return accountingRecordService.getAllCarbonAccountingForReview();
    }

    /**
     * 管理员获取证明材料文件流
     *
     * @param id 碳核算请求id
     * @return 证明材料文件流 ResponseEntity<InputStreamResource>
     */
    @GetMapping("/administrator/accounting_record/file")
    public ResponseEntity<InputStreamResource> getSupportingMaterial(@PathParam("id") int id) throws IOException {
        return accountingRecordService.getSupportingMaterial(id);
    }

    /**
     * 管理员获取所有还未被审核的注册申请
     *
     * @return 未审核的注册申请 List<RegisterApplication>
     */
    @GetMapping("/administrator/application/review")
    public CommonResponse<List<RegisterApplication>> getPendingReviewAccount() {
        return accountService.getPendingReviewAccount();
    }

    /**
     * 同意注册
     * @param register_application_id 注册表的id
     * @param account_id 管理人员的id
     * @return 注册结果 CommonResponse<String>
     */
    @PostMapping("/administrator/application")
    public CommonResponse<String> AgreeApplication(@PathParam("register_application_id") int register_application_id, @PathParam("account_id") int account_id) {
        return accountService.AgreeApplication(register_application_id, account_id);
    }

    /**
     * 同意修改账号信息
     *
     * @param phone 手机号
     * @param email 邮箱
     * @return 修改结果 CommonResponse<String>
     */

    @PostMapping("/administrator/update/account_info")
    public CommonResponse<String> AgreeUpdateAccountInfo(@PathParam("phone") String phone, @PathParam("email") String email) {
        return accountService.AgreeUpdateAccountInfo(phone, email);
    }

    /**
     * 拒绝注册
     * @param register_application_id 注册表的id
     * @param account_id 管理人员的id
     * @return 注册结果 CommonResponse<String>
     */
    @DeleteMapping("/administrator/application")
    public CommonResponse<String> RefuseApplication(@PathParam("register_application_id") int register_application_id, @PathParam("account_id") int account_id) {
        return accountService.RefuseApplication(register_application_id, account_id);
    }


    /**
     * 拒绝修改账号信息
     *
     * @param phone 手机号
     * @param email 邮箱
     * @return 修改结果 CommonResponse<String>
     */
    @DeleteMapping("/administrator/update/account_info")
    public CommonResponse<String> RefuseUpdateAccountInfo(@PathParam("phone") String phone, @PathParam("email") String email) {
        return accountService.RefuseUpdateAccountInfo(phone, email);
    }

}
