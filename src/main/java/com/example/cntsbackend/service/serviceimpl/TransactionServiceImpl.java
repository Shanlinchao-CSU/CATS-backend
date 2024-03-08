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

    //企业查看关于企业自己发布的交易信息
    public CommonResponse<List<Transaction>> getTransactionData(String account_name) {
        List<Transaction> transactions = transactionMapper.selectList(new QueryWrapper<Transaction>().eq("from", account_name));
        return CommonResponse.createForSuccess(transactions);
    }

    //第三方监管机构和管理员查看交易信息
    public CommonResponse<List<Transaction>> getAllTransactionDatas() {
        List<Transaction> transactions = transactionMapper.selectList(null);
        return CommonResponse.createForSuccess(transactions);
    }

    public CommonResponse<List<Transaction>> getAllUnfinishTransactionDatas() {
        List<Transaction> transactions = transactionMapper.selectList(new QueryWrapper<Transaction>().eq("state",false));
        return CommonResponse.createForSuccess(transactions);
    }

    public CommonResponse<List<Transaction>> getMyFinishedTransactionDatas(String account_name){
        QueryWrapper<Transaction> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("t_from",account_name ).or().eq("t_to", account_name))
                .eq("state", 1);
        List<Transaction> transactions = transactionMapper.selectList(queryWrapper);
        return CommonResponse.createForSuccess(transactions);
    }
    public CommonResponse<String> PublishTransaction(String name ,double amount , double unit_price){
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_name", name));
        int account_id = account.getAccount_id();
        CMessage cMessage = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", account_id));
        if(cMessage.getT_remain()>=amount){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatedDateTime = format.format(new Date());
            Date timestamp = Timestamp.valueOf(formatedDateTime);
            //数据库增添一行数据
            transactionMapper.insert(new Transaction(name,"",amount,unit_price,timestamp,null,false));
            //CMessage更新数据
            double limit = cMessage.getT_remain()-amount;
            cMessage.setT_remain(limit);
            UpdateWrapper<CMessage> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("account_id",account_id);
            cMessageMapper.update(cMessage, updateWrapper);

            return CommonResponse.createForSuccess("发布交易信息成功");
        }
        return CommonResponse.createForError("发布交易信息失败，额度不足");
    }

    public CommonResponse<String> CompleteTransaction(String name ,int transaction_id){
        //获取买额度企业信息
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_name", name));
        int account_id = account.getAccount_id();
        CMessage cMessage = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", account_id));
        Transaction transaction = transactionMapper.selectOne(new QueryWrapper<Transaction>().eq("transaction_id", transaction_id));
        double amount = transaction.getAmount();
        double unit_price = transaction.getUnit_price();

        if(cMessage.getT_coin()>=(amount * unit_price)){//TODO:用碳币来判断
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
            double coin = cMessage.getT_coin()-amount * unit_price;
            double t_remain = cMessage.getT_remain() + amount;
            cMessage.setT_coin(coin);
            cMessage.setT_remain(t_remain);
            UpdateWrapper<CMessage> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("account_id",account_id);
            cMessageMapper.update(cMessage, updateWrapper1);
            //TODO:增加碳币
            //获得发布企业的信息
            String t_from = transaction.getT_from();
            Account account1 = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_name", t_from));
            int account_id1 = account1.getAccount_id();
            CMessage cMessage1 = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", account_id1));
            double t_coin = cMessage1.getT_coin();
            t_coin = t_coin + amount * unit_price;
            cMessage1.setT_coin(t_coin);
            UpdateWrapper<CMessage> updateWrapper2 = new UpdateWrapper<>();
            updateWrapper2.eq("account_id",account_id1);
            cMessageMapper.update(cMessage1, updateWrapper2);

            return CommonResponse.createForSuccess("交易成功");
        }
        return CommonResponse.createForError("交易失败，额度不足");
    }

    public CommonResponse<String> cancelTransactionData(int transaction_id){
        Transaction transaction = transactionMapper.selectOne(new QueryWrapper<Transaction>().eq("transaction_id", transaction_id));
        String t_from = transaction.getT_from();
        double amount = transaction.getAmount();
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_name", t_from));
        int account_id = account.getAccount_id();
        CMessage cMessage = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", account_id));
        double t_remain = cMessage.getT_remain();
        t_remain = t_remain + amount;
        cMessage.setT_remain(t_remain);
        UpdateWrapper<CMessage> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.eq("account_id",account_id);
        cMessageMapper.update(cMessage, updateWrapper1);
        transactionMapper.deleteById(transaction);
        return CommonResponse.createForSuccess("取消发布交易信息成功");
    }
}
