package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.dto.TransactionDto;

import java.util.List;

public interface TransactionService {

    //TODO:(修改)第三方监管机构和管理员查看交易信息
    CommonResponse<List<TransactionDto>> getAllTransactionDatas();
    //TODO:(修改)企业获取自己已完成的交易
    CommonResponse<List<TransactionDto>> getMyFinishedTransactionDatas(int account_id);
    //TODO:(新加)额度购买，第一个参数为买家id，第二个参数为额度发布信息的id，第三个参数为要买的额度
    CommonResponse<String> CompleteTransaction(int account_id ,int quotaSale_id , double amount);
}
