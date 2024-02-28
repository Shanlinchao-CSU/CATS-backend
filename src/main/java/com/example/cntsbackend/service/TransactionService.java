package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Transaction;

import java.util.List;

public interface TransactionService {
    //企业查看交易信息
    CommonResponse<List<Transaction>> getTransactionData(String account_name);
    //第三方监管机构和管理员查看交易信息
    CommonResponse<List<Transaction>> getAllTransactionDatas();
}
