package com.example.cntsbackend.controller;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * ThirdPartyRegulators 第三方监管机构controller
 */

@Controller("/thirdParty")
public class ThirdPartyRegulatorsController {

    @Autowired
    private TransactionService transactionService;

    /**
     * 第三方监管机构查看交易信息
     * @return 交易信息 List<Transaction>
     */
    @GetMapping("/transaction")
    public CommonResponse<List<Transaction>> getAllTransactionDatas() {
        return transactionService.getAllTransactionDatas();
    }

}
