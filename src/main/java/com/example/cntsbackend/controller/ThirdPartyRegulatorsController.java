package com.example.cntsbackend.controller;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.dto.TransactionDto;
import com.example.cntsbackend.service.RegisterApplicationService;
import com.example.cntsbackend.service.TransactionService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;

/**
 * ThirdPartyRegulators 第三方监管机构controller
 */

@RestController
public class ThirdPartyRegulatorsController {

    @Autowired
    private RegisterApplicationService registerApplicationService;

    @Autowired
    private TransactionService transactionService;

    /**
     * 第三方监管机构查看交易信息
     *
     * @return 交易信息 List<Transaction>
     */
    @GetMapping("/thirdParty/transaction")
    public CommonResponse<List<TransactionDto>> getAllTransactionDatas() {
        return transactionService.getAllTransactionDatas();
    }

    /**
     * 第三方监管机构注册
     *
     * @param file     文件
     * @param name     名字
     * @param password 密码
     * @param phone    电话
     * @param type     类型
     * @return 注册结果 CommonResponse<String>
     */
    @PostMapping("/thirdParty/info")
    public CommonResponse<String> register(@PathParam("file") File file, @PathParam("name") String name, @PathParam("password") String password, @PathParam("phone") String phone, @PathParam("type") int type) {
        return registerApplicationService.ThirdPartyRegulatorsRegister(file, name, password, phone, type);
    }

}
