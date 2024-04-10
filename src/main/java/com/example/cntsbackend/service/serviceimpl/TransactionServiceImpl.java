package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.CMessage;
import com.example.cntsbackend.domain.QuotaSale;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.dto.TransactionDto;
import com.example.cntsbackend.persistence.AccountMapper;
import com.example.cntsbackend.persistence.CMessageMapper;
import com.example.cntsbackend.persistence.QuotaSaleMapper;
import com.example.cntsbackend.persistence.TransactionMapper;
import com.example.cntsbackend.service.TransactionService;
import com.example.cntsbackend.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CMessageMapper cMessageMapper;
    @Autowired
    private QuotaSaleMapper quotaSaleMapper;

    //第三方监管机构和管理员查看交易信息
    public CommonResponse<List<TransactionDto>> getAllTransactionDatas() {
        List<Transaction> transactions = transactionMapper.selectList(null);
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            int sale_id = transaction.getSale_id();
            int buyer_id = transaction.getBuyer_id();
            Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", sale_id));
            String sale_account_name = account.getAccount_name();
            Account account1 = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", buyer_id));
            String buyer_account_name = account1.getAccount_name();
            TransactionDto transactionDto = new TransactionDto(transaction.getTransaction_id(), transaction.getAmount(), sale_account_name, buyer_account_name, transaction.getCost(), transaction.getComplete_time());
            transactionDtoList.add(transactionDto);
        }
        return CommonResponse.createForSuccess("获取上月交易信息成功",transactionDtoList);
    }

    public CommonResponse<List<TransactionDto>> getMyFinishedTransactionDatas(int account_id){
        QueryWrapper<Transaction> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("buyer_id",account_id ).or().eq("sale_id", account_id));
        List<Transaction> transactions = transactionMapper.selectList(queryWrapper);
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            int sale_id = transaction.getSale_id();
            int buyer_id = transaction.getBuyer_id();
            Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", sale_id));
            String sale_account_name = account.getAccount_name();
            Account account1 = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", buyer_id));
            String buyer_account_name = account1.getAccount_name();
            TransactionDto transactionDto = new TransactionDto(transaction.getTransaction_id(), transaction.getAmount(), sale_account_name, buyer_account_name, transaction.getCost(), transaction.getComplete_time());
            transactionDtoList.add(transactionDto);
        }
        return CommonResponse.createForSuccess(transactionDtoList);
    }

    public CommonResponse<String> CompleteTransaction(int account_id ,int quotaSale_id , double amount){
        QuotaSale quotaSale = quotaSaleMapper.selectById(quotaSale_id);
        double quota = quotaSale.getQuota();
        synchronized(this){
            if(amount > quota){
                return CommonResponse.createForError("购买额度失败,购买额度超额");
            }
            quota = BigDecimalUtil.subtract(quota,amount);
            if(quota == 0){
                quotaSaleMapper.deleteById(quotaSale);
            }else{
                quotaSale.setQuota(quota);
                quotaSaleMapper.updateById(quotaSale);
            }
            return CommonResponse.createForSuccess("交易成功");
        }
    }

    public CommonResponse<String> GetTransactionHash(int transaction_id , String hash){
        Transaction transaction = transactionMapper.selectOne(new QueryWrapper<Transaction>().eq("transaction_id", transaction_id));
        transaction.setTransaction_hash(hash);
        transactionMapper.updateById(transaction);
        return CommonResponse.createForSuccess("获取交易hash成功");
    }
}
