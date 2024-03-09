package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.CMessage;
import com.example.cntsbackend.domain.QuotaSale;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.persistence.AccountMapper;
import com.example.cntsbackend.persistence.CMessageMapper;
import com.example.cntsbackend.persistence.QuotaSaleMapper;
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
    @Autowired
    private QuotaSaleMapper quotaSaleMapper;

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

//    public CommonResponse<String> CompleteTransaction(int account_id ,int quotaSale_id , double amount){
//        //获取买额度企业信息
//        CMessage cMessage = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", account_id));
//        QuotaSale quotaSale = quotaSaleMapper.selectById(quotaSale_id);
//        double quota = quotaSale.getQuota();
//        double unit_price = quotaSale.getUnit_price();
//        int seller_id = quotaSale.getSeller_id();
//        if(cMessage.getT_coin()>=(amount * unit_price)){
//            //完成交易
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String formatedDateTime = format.format(new Date());
//            Date timestamp = Timestamp.valueOf(formatedDateTime);
//            double cost = amount * unit_price;
//            Transaction transaction = new Transaction(amount,account_id,seller_id,cost,timestamp);
//            transactionMapper.insert(transaction);
//            //更新买家信息
//            double t_remain = cMessage.getT_remain();
//            t_remain = t_remain + amount;
//            double t_coin = cMessage.getT_coin();
//            t_coin = t_coin - (amount * unit_price);
//            cMessage.setT_remain(t_remain);
//            cMessage.setT_coin(t_coin);
//            UpdateWrapper<CMessage> updateWrapper = new UpdateWrapper<>();
//            updateWrapper.eq("account_id",account_id);
//            cMessageMapper.update(cMessage, updateWrapper);
//            //更新卖家信息
//            CMessage cMessage1 = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", seller_id));
//            double t_coin1 = cMessage1.getT_coin();
//            t_coin1 = t_coin1 + (amount * unit_price);
//            cMessage1.setT_coin(t_coin1);
//            UpdateWrapper<CMessage> updateWrapper1 = new UpdateWrapper<>();
//            updateWrapper1.eq("account_id",seller_id);
//            cMessageMapper.update(cMessage1, updateWrapper1);
//
//            quota = quota - amount;
//            if(quota == 0){
//                quotaSaleMapper.deleteById(quotaSale);
//            }else{
//                quotaSale.setQuota(quota);
//                UpdateWrapper<QuotaSale> updateWrapper2 = new UpdateWrapper<>();
//                updateWrapper2.eq("seller_id",seller_id);
//                quotaSaleMapper.update(quotaSale, updateWrapper2);
//            }
//            return CommonResponse.createForSuccess("交易成功");
//        }else {
//            return CommonResponse.createForError("交易失败，额度不足");
//        }
//    }
}
