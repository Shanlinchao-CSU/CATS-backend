package com.example.cntsbackend.controller;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.service.RegisterApplicationService;
import com.example.cntsbackend.service.TransactionService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

/**
 * EnterpriseUser 企业用户controller
 */
@RestController
public class EnterpriseUserController {

    @Autowired
    private RegisterApplicationService registerApplicationService;
    @Autowired
    private TransactionService transactionService;



    /**
     * 企业获取所有未完成交易的交易信息(便于额度购买选择)
     *
     * @return 交易信息 List<Transaction>
     */
    @GetMapping("/enterprise/transaction/unfinished")
    public CommonResponse<List<Transaction>> getAllUnfinishTransactionDatas() {
        return transactionService.getAllUnfinishTransactionDatas();
    }

    /**
     * 企业获取自己已完成的交易
     *
     * @param account_name 账号
     * @return 交易信息 List<Transaction>
     */
    @GetMapping("/enterprise/transaction/finished/{account_name}")
    public CommonResponse<List<Transaction>> getMyFinishedTransactionDatas(@PathVariable("account_name") String account_name) {
        return transactionService.getMyFinishedTransactionDatas(account_name);
    }


    /**
     * 企业用户注册
     *
     * @param file            文件
     * @param name            账号
     * @param password        密码
     * @param phone           电话
     * @param email           邮箱
     * @param enterprise_type 企业类型
     * @param type            类型
     * @return 注册结果 CommonResponse<String>
     */
    @PostMapping("/enterprise/info")
    public CommonResponse<String> register(@PathParam("file") File file, @PathParam("name") String name, @PathParam("password") String password, @PathParam("phone") String phone, @PathParam("email") String email, @PathParam("enterprise_type") int enterprise_type, @PathParam("type") int type) {
        return registerApplicationService.EnterpriseUserRegister(file, name, password, phone, email, enterprise_type, type);
    }

}
