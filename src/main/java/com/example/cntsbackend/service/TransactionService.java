package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Transaction;

import java.util.List;

public interface TransactionService {

    //第三方监管机构和管理员查看交易信息
    CommonResponse<List<Transaction>> getAllTransactionDatas();
    //企业获取所有未完成交易的交易信息(便于额度购买选择)
    CommonResponse<List<Transaction>> getAllUnfinishTransactionDatas();
    //企业获取自己已完成的交易
    CommonResponse<List<Transaction>> getMyFinishedTransactionDatas(String account_name);
    //TODO:(新加)额度购买，第一个参数为买家id，第二个参数为发布信息的id，第三个参数为要买的额度
//    CommonResponse<String> CompleteTransaction(int account_id ,int quotaSale_id , double amount);
}
