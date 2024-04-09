package com.example.cntsbackend.controller;

import com.example.cntsbackend.annotation.LOG;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.RegisterApplication;
import com.example.cntsbackend.dto.AccountDto;
import com.example.cntsbackend.dto.AccountingRecordDto;
import com.example.cntsbackend.dto.TransactionDto;
import com.example.cntsbackend.service.AccountService;
import com.example.cntsbackend.service.AccountingRecordService;
import com.example.cntsbackend.service.RegisterApplicationService;
import com.example.cntsbackend.service.TransactionService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Administrator 管理员controller
 */
@RestController
@RequestMapping("/api")
public class AdministratorController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountingRecordService accountingRecordService;

    @Autowired
    private RegisterApplicationService registerApplicationService;

    private static final String MODULE_NAME = "管理员模块";
    private static final String MODULE_VERSION = "v1.0.0";

    /**
     * 第三方监管机构和管理员查看交易信息
     *
     * @return 交易信息 List<Transaction>
     */
    @GetMapping("/administrator/transaction")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<List<TransactionDto>> getAllTransactionDatas() {
        return transactionService.getAllTransactionDatas();
    }

    /**
     * 获取所有待审核的碳核算请求
     *
     * @return 待审核的碳核算请求 List<AccountingRecordDto>
     */
    @GetMapping("/administrator/accounting_record/review")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<List<AccountingRecordDto>> getAllCarbonAccountingForReview() throws Exception {
        return accountingRecordService.getAllCarbonAccountingForReview();
    }

    /**
     * 获取所有碳核算请求
     *
     * @return 碳核算请求 List<AccountingRecordDto>
     */
    @GetMapping("/administrator/accounting_record")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<List<AccountingRecordDto>> getAllCarbonAccounting() {
        return accountingRecordService.getAllCarbonAccounting();
    }


    /**
     * 管理员获取证明材料文件流
     *
     * @param id 碳核算请求id
     */
    @GetMapping("/administrator/accounting_record/file")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public void getSupportingMaterial(
            @PathParam("id") int id,
            HttpServletResponse response) throws Exception {
        accountingRecordService.getSupportingMaterial(id,response);
    }

    /**
     * 管理员获取注册申请的文件流
     *
     * @param id 注册申请id
     */
    @GetMapping("/administrator/application/file")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public void getRegisterSupportingMaterial(
            @PathParam("id") int id,
            HttpServletResponse response) throws Exception {
        registerApplicationService.getRegisterSupportingMaterial(id,response);
    }

    /**
     * 管理员获取所有还未被审核的注册申请
     *
     * @return 未审核的注册申请 List<RegisterApplication>
     */
    @GetMapping("/administrator/application/review")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<List<RegisterApplication>> getPendingReviewAccount() throws Exception {
        return accountService.getPendingReviewAccount();
    }

    /**
     * 管理员获取所有企业用户
     *
     * @return 所有企业用户 List<AccountDto>
     */
    @GetMapping("/administrator/enterprise/accounts")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<List<AccountDto>> getAllEnterpriseUsers() throws Exception {
        return accountService.getAllEnterpriseUsers();
    }

    /**
     * 管理员获取各月超额企业信息
     *
     * @return 所有企业用户 List<AccountDto>
     */
    @GetMapping("/administrator/enterprise/exceed")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<List<AccountDto>> getExceedEnterpriseUsers() throws Exception {
        return accountService.GetAllExcessEnterprises();
    }


    /**
     * 管理员修改企业额度
     *
     * @param account_id 企业账号ID
     * @param t_limit    额度
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/administrator/enterprise/t_limit")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> ModifyT_limit(
            @PathParam("account_id") int account_id,
            @PathParam("t_limit") double t_limit) {
        return accountService.ModifyT_limit(account_id, t_limit);
    }

    /**
     * 同意注册
     * @param register_application_id 注册表的id
     * @param account_id 管理人员的id
     * @return 注册结果 CommonResponse<String>
     */
    @PostMapping("/administrator/application")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> AgreeApplication(
            @PathParam("register_application_id") int register_application_id,
            @PathParam("account_id") int account_id) throws Exception {
        return accountService.AgreeApplication(register_application_id, account_id);
    }

    /**
     * 同意修改账号信息
     *
     * @param account_id 账号ID
     * @return 修改结果 CommonResponse<String>
     */

    @PostMapping("/administrator/update/account_info")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> AgreeUpdateAccountInfo(
            @PathParam("account_id") int account_id) {
        return accountService.AgreeUpdateAccountInfo(account_id);
    }


    /**
     * 拒绝注册
     * @param register_application_id 注册表的id
     * @param account_id 管理人员的id
     * @return 注册结果 CommonResponse<String>
     */
    @DeleteMapping("/administrator/application")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> RefuseApplication(
            @PathParam("register_application_id") int register_application_id,
            @PathParam("account_id") int account_id) {
        return accountService.RefuseApplication(register_application_id, account_id);
    }


    /**
     * 拒绝修改账号信息
     *
     * @param account_id 账号ID
     * @return 修改结果 CommonResponse<String>
     */
    @DeleteMapping("/administrator/update/account_info")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> RefuseUpdateAccountInfo(
            @PathParam("account_id") int account_id) {
        return accountService.RefuseUpdateAccountInfo(account_id);
    }

}
