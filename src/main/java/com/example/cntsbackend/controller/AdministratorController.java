package com.example.cntsbackend.controller;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.AccountingRecord;
import com.example.cntsbackend.domain.Transaction;
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

import java.io.IOException;
import java.util.List;

/**
 * Administrator 管理员controller
 */
@Controller
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
    public CommonResponse<List<Transaction>> getAllTransactionDatas() {
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
     * 同意注册
     *
     * @param phone 手机号
     * @param email 邮箱
     * @return 注册结果 CommonResponse<String>
     */
    @PostMapping("/administrator/application")
    public CommonResponse<String> AgreeApplication(@PathParam("phone") String phone, @PathParam("email") String email) {
        return accountService.AgreeApplication(phone, email);
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
     *
     * @param phone 手机号
     * @param email 邮箱
     * @return 注册结果 CommonResponse<String>
     */
    @DeleteMapping("/administrator/application")
    public CommonResponse<String> RefuseApplication(@PathParam("phone") String phone, @PathParam("email") String email) {
        return accountService.RefuseApplication(phone, email);
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
