package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.persistence.AccountMapper;
import com.example.cntsbackend.persistence.TransactionMapper;
import com.example.cntsbackend.service.TransactionService;
import com.example.cntsbackend.util.SendMailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionMapper transactionMapper;
    //企业查看交易信息
    public CommonResponse<List<Transaction>> getTransactionData(String account_name) {
        List<Transaction> transactions = transactionMapper.selectList(new QueryWrapper<Transaction>().eq("from", account_name).or().eq("to", account_name));
        return CommonResponse.createForSuccess(transactions);
    }

    //第三方监管机构和管理员查看交易信息
    public CommonResponse<List<Transaction>> getAllTransactionDatas() {
        List<Transaction> transactions = transactionMapper.selectList(null);
        return CommonResponse.createForSuccess(transactions);
    }
}
