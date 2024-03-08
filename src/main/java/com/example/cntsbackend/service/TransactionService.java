package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Transaction;

import java.util.List;

public interface TransactionService {
    //TODO:（改动，之前是查看关于自己的发布或者购买）企业查看自己发布的交易信息
    CommonResponse<List<Transaction>> getTransactionData(String account_name);
    //第三方监管机构和管理员查看交易信息
    CommonResponse<List<Transaction>> getAllTransactionDatas();
    //TODO:（新加）企业获取所有未完成交易的交易信息(便于额度购买选择)
    CommonResponse<List<Transaction>> getAllUnfinishTransactionDatas();
    //企业获取自己已完成的交易
    CommonResponse<List<Transaction>> getMyFinishedTransactionDatas(String account_name);
    //TODO：（改动，新加交易单价）发布交易信息
    CommonResponse<String> PublishTransaction(String name ,double amount ,double unit_price);
    //额度购买
    CommonResponse<String> CompleteTransaction(String name ,int transaction_id);
    //TODO：（新加）企业取消已发布的交易信息
    CommonResponse<String> cancelTransactionData(int transaction_id);
    //TODO:（新加）企业修改单价
    CommonResponse<String> ModifyUnitPrice(int transaction_id,double unit_price);
}
