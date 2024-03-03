package com.example.cntsbackend.controller;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.service.RegisterApplicationService;
import com.example.cntsbackend.service.TransactionService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.util.List;

/**
 * EnterpriseUser 企业用户controller
 */
@Controller
public class EnterpriseUserController {

    @Autowired
    private RegisterApplicationService registerApplicationService;
    @Autowired
    private TransactionService transactionService;


    /**
     * 企业查看交易信息
     *
     * @param id 账号
     * @return 交易信息 List<Transaction>
     */
    @GetMapping("/enterprise/transaction/{id}")
    public CommonResponse<List<Transaction>> getTransaction(@PathVariable("id") String id) {
        return transactionService.getTransactionData(id);
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
    public CommonResponse<String> register(@PathParam("file") File file, @PathParam("name") String name, @PathParam("password") String password, @PathParam("phone") String phone, @PathParam("email") String email, @PathParam("enterprise_type") int enterprise_type, @PathParam("type") int type, @PathParam("enterprise_address") String enterprise_address) {
        return registerApplicationService.EnterpriseUserRegister(file, name, password, phone, email, enterprise_type, type, enterprise_address);
    }

    /**
     * 发布交易信息
     *
     * @param name   账号
     * @param amount 金额
     * @return 发布结果 CommonResponse<String>
     */
    @PostMapping("/enterprise/transaction")
    public CommonResponse<String> publishTransaction(@PathParam("name") String name, @PathParam("amount") int amount) {
        return transactionService.PublishTransaction(name, amount);
    }

    /**
     * 完成交易
     *
     * @param name          账号
     * @param transaction_id 交易id
     * @return 完成结果 CommonResponse<String>
     */
    @PatchMapping("/enterprise/transaction")
    public CommonResponse<String> completeTransaction(@PathParam("name") String name, @PathParam("transaction_id") int transaction_id) {
        return transactionService.CompleteTransaction(name, transaction_id);
    }
}
