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

    public synchronized CommonResponse<String> CompleteTransaction(int account_id ,int quotaSale_id , double amount){
        //获取买额度企业信息
        CMessage cMessage = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", account_id));
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", account_id));
        QuotaSale quotaSale = quotaSaleMapper.selectById(quotaSale_id);
        double quota = quotaSale.getQuota();
        double unit_price = quotaSale.getUnit_price();
        int seller_id = quotaSale.getSeller_id();
        double cost = BigDecimalUtil.multiply(amount, unit_price);
        //获取卖家信息
        Account account1 = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", seller_id));
        if(account == null ){
            return CommonResponse.createForError("买额度企业用户不存在");
        }
        if (account1 == null){
            return CommonResponse.createForError("卖额度企业用户不存在");
        }
        if(BigDecimalUtil.compareTo(account.getT_coin(),cost)>=0){
            //完成交易
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatedDateTime = format.format(new Date());
            Date timestamp = Timestamp.valueOf(formatedDateTime);
//            double cost = amount * unit_price;
            Transaction transaction = new Transaction(amount,account_id,seller_id,cost,timestamp);
            transactionMapper.insert(transaction);
            //更新买家信息
            double t_remain = cMessage.getT_remain();
            t_remain = BigDecimalUtil.add(t_remain,amount);
//            t_remain = t_remain + amount;
            cMessage.setT_remain(t_remain);
            UpdateWrapper<CMessage> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("account_id",account_id);
            cMessageMapper.update(cMessage, updateWrapper);
            double t_coin = account.getT_coin();
            t_coin = BigDecimalUtil.subtract(t_coin,cost);
//            t_coin = t_coin - (amount * unit_price);
            account.setT_coin(t_coin);
            UpdateWrapper<Account> updateWrapper3 = new UpdateWrapper<>();
            updateWrapper3.eq("account_id",account_id);
            accountMapper.update(account, updateWrapper3);
            //更新卖家信息
            double t_coin1 = account1.getT_coin();
            t_coin1 = BigDecimalUtil.add(t_coin1,cost);
//            t_coin1 = t_coin1 + (amount * unit_price);
            account1.setT_coin(t_coin1);
            UpdateWrapper<Account> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("account_id",seller_id);
            accountMapper.update(account1, updateWrapper1);

            quota = BigDecimalUtil.subtract(quota,amount);
//            quota = quota - amount;
            if(quota == 0){
                quotaSaleMapper.deleteById(quotaSale);
            }else{
                quotaSale.setQuota(quota);
                UpdateWrapper<QuotaSale> updateWrapper2 = new UpdateWrapper<>();
                updateWrapper2.eq("seller_id",seller_id);
                quotaSaleMapper.update(quotaSale, updateWrapper2);
            }
            return CommonResponse.createForSuccess("交易成功");
        }else {
            return CommonResponse.createForError("交易失败，额度不足");
        }
    }
}
