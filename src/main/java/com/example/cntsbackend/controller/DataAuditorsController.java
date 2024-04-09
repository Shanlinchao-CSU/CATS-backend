package com.example.cntsbackend.controller;

import com.example.cntsbackend.annotation.LOG;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.dto.AccountingRecordDto;
import com.example.cntsbackend.service.AccountingRecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DataAuditors 数据审核员controller
 */
@RestController
public class DataAuditorsController {

    @Autowired
    private AccountingRecordService accountingRecordService;

    private static final String MODULE_NAME = "数据审核员模块";
    private static final String MODULE_VERSION = "v1.0.0";


    /**
     * 数据审核员获取自己审核的所有碳核算申请
     *
     * @param account_id    数据审核员id
     * @return              返回所有碳核算申请
     */
    @GetMapping("/dataAuditors/carbon_accounting")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<List<AccountingRecordDto>> DataAuditorsGetMyCarbonAccounting(
            @PathParam("conductor_id") int account_id) {
        return accountingRecordService.DataAuditorsGetMyCarbonAccounting(account_id);
    }


    /**
     * 数据审核员处理碳核算请求
     *
     * @param id            碳请求的id
     * @param approve       是否通过
     * @param conductor_id  数据审核员id
     * @return              返回处理结果
     */
    @PostMapping("/dataAuditors/carbon_accounting")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> CarbonAccountingRequests(
            @PathParam("id") int id,
            @PathParam("approve") boolean approve,
            @PathParam("conductor_id") int conductor_id) {
        return accountingRecordService.CarbonAccountingRequests(id, approve, conductor_id);
    }

    @GetMapping("/dataAuditors/verify_result")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<Double> VerifyResult(
            @PathParam("id") int id) throws JsonProcessingException {
        return accountingRecordService.CarbonAccounting(id);
    }

}
