package com.example.cntsbackend.controller;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.dto.AccountingRecordDto;
import com.example.cntsbackend.service.AccountingRecordService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DataAuditors 数据审核员controller
 */
@RestController
public class DataAuditorsController {

    @Autowired
    private AccountingRecordService accountingRecordService;


    /**
     * 数据审核员获取自己审核的所有碳核算申请
     *
     * @param account_id    数据审核员id
     * @return              返回所有碳核算申请
     */
    @GetMapping("/dataAuditors/carbon_accounting")
    public CommonResponse<List<AccountingRecordDto>> DataAuditorsGetMyCarbonAccounting(
            @PathParam("conductor_id") int account_id) {
        return accountingRecordService.DataAuditorsGetMyCarbonAccounting(account_id);
    }

}
