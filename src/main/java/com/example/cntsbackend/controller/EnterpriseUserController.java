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
@Controller
public class EnterpriseUserController {

    @Autowired
    private RegisterApplicationService registerApplicationService;
    @Autowired
    private TransactionService transactionService;


    /**
     * 企业查看交易信息
     *
     * @param account_name 账号
     * @return 交易信息 List<Transaction>
     */
    @GetMapping("/enterprise/transaction/{account_name}")
    public CommonResponse<List<Transaction>> getTransaction(@PathVariable("account_name") String account_name) {
        return transactionService.getTransactionData(account_name);
    }

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
    public CommonResponse<String> register(@PathParam("file") File file, @PathParam("name") String name, @PathParam("password") String password, @PathParam("phone") String phone, @PathParam("email") String email, @PathParam("enterprise_type") int enterprise_type, @PathParam("type") int type, @PathParam("enterprise_address") String enterprise_address) {
        return registerApplicationService.EnterpriseUserRegister(file, name, password, phone, email, enterprise_type, type, enterprise_address);
    }

    /**
     * 发布交易信息
     *
     * @param name   账号
     * @param amount 数量
     * @param unit_price 单价
     * @return 发布结果 CommonResponse<String>
     */
    @PostMapping("/enterprise/transaction")
    public CommonResponse<String> publishTransaction(@PathParam("name") String name, @PathParam("amount") int amount, @PathParam("unit_price") int unit_price) {
        return transactionService.PublishTransaction(name, amount, unit_price);
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

    /**
     * 修改单价
     *
     * @param transaction_id 交易id
     * @param unit_price 单价
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/enterprise/transaction/price")
    public CommonResponse<String> modifyUnitPrice(@PathParam("transaction_id") int transaction_id, @PathParam("unit_price") int unit_price) {
        return transactionService.ModifyUnitPrice(transaction_id, unit_price);
    }

    /**
     * 取消交易
     *
     * @param transaction_id 交易id
     * @return 取消结果 CommonResponse<String>
     */
    @DeleteMapping("/enterprise/transaction")
    public CommonResponse<String> cancelTransaction(@PathParam("transaction_id") int transaction_id) {
        return transactionService.cancelTransactionData(transaction_id);
    }
}
