package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.CMessage;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.persistence.AccountMapper;
import com.example.cntsbackend.persistence.CMessageMapper;
import com.example.cntsbackend.persistence.TransactionMapper;
import com.example.cntsbackend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
    public CommonResponse<String> PublishTransaction(String name ,int amount){
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_name", name));
        int account_id = account.getAccount_id();
        CMessage cMessage = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", account_id));
        if(cMessage.getClimit()>=amount){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatedDateTime = format.format(new Date());
            Date timestamp = Timestamp.valueOf(formatedDateTime);
            //数据库增添一行数据
            transactionMapper.insert(new Transaction(name,"",amount,timestamp,null,false));
            //CMessage更新数据
            int limit = cMessage.getClimit()-amount;
            cMessage.setClimit(limit);
            UpdateWrapper<CMessage> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("account_id",account_id);
            cMessageMapper.update(cMessage, updateWrapper);

            return CommonResponse.createForSuccess("发布交易信息成功");
        }
        return CommonResponse.createForError("发布交易信息失败，额度不足");
    }

    public CommonResponse<String> CompleteTransaction(String name ,int transaction_id){
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_name", name));
        int account_id = account.getAccount_id();
        CMessage cMessage = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", account_id));
        Transaction transaction = transactionMapper.selectOne(new QueryWrapper<Transaction>().eq("transaction_id", transaction_id));
        int amount = transaction.getAmount();
        if(cMessage.getClimit()>=amount){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatedDateTime = format.format(new Date());
            Date timestamp = Timestamp.valueOf(formatedDateTime);

            transaction.setT_to(name);
            transaction.setComplete_time(timestamp);
            transaction.setState(true);

            UpdateWrapper<Transaction> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("transaction_id",transaction_id);
            transactionMapper.update(transaction, updateWrapper);

            //CMessage更新数据
            int limit = cMessage.getClimit()-amount;
            cMessage.setClimit(limit);
            UpdateWrapper<CMessage> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("account_id",account_id);
            cMessageMapper.update(cMessage, updateWrapper1);
            //TODO:增加碳币
            return CommonResponse.createForSuccess("交易成功");
        }
        return CommonResponse.createForError("发布交易信息失败，额度不足");
    }
}
