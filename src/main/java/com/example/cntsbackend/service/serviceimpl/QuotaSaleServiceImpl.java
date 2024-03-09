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
import com.example.cntsbackend.service.QuotaSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class QuotaSaleServiceImpl implements QuotaSaleService {
    @Autowired
    private CMessageMapper cMessageMapper;
    @Autowired
    private QuotaSaleMapper quotaSaleMapper;
    @Autowired
    private AccountMapper accountMapper;

    public CommonResponse<String> PublishTransaction(int account_id , double quota , double unit_price) {
        CMessage cMessage = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", account_id));
        double t_remain = cMessage.getT_remain();
        if(t_remain>=quota){
            //数据库增添一行数据
            QuotaSale quotaSale = new QuotaSale(quota,account_id,unit_price);
            quotaSaleMapper.insert(quotaSale);
            //CMessage更新数据
            t_remain = t_remain - quota;
            cMessage.setT_remain(t_remain);
            UpdateWrapper<CMessage> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("account_id",account_id);
            cMessageMapper.update(cMessage, updateWrapper);
            return CommonResponse.createForSuccess("发布交易信息成功");
        }
        return CommonResponse.createForError("发布交易信息失败，额度不足");
    }

    public CommonResponse<String> cancelTransactionData(int id){
        QuotaSale quotaSale = quotaSaleMapper.selectOne(new QueryWrapper<QuotaSale>().eq("id", id));
        double quota = quotaSale.getQuota();
        int seller_id = quotaSale.getSeller_id();
        CMessage cMessage = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", seller_id));
        double t_remain = cMessage.getT_remain();
        t_remain = t_remain + quota;
        cMessage.setT_remain(t_remain);
        UpdateWrapper<CMessage> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("account_id",seller_id);
        cMessageMapper.update(cMessage, updateWrapper);
        quotaSaleMapper.deleteById(quotaSale);
        return CommonResponse.createForSuccess("取消发布交易信息成功");

    }

    public CommonResponse<String> ModifyUnitPrice(int id,double unit_price) {
        QuotaSale quotaSale = quotaSaleMapper.selectOne(new QueryWrapper<QuotaSale>().eq("id", id));
        quotaSale.setUnit_price(unit_price);
        UpdateWrapper<QuotaSale> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        quotaSaleMapper.update(quotaSale, updateWrapper);
        return CommonResponse.createForSuccess("修改单价成功");
    }

    public CommonResponse<Double> getRemain(int account_id){
        QuotaSale quotaSale = quotaSaleMapper.selectOne(new QueryWrapper<QuotaSale>().eq("seller_id", account_id));
        double quota = quotaSale.getQuota();//TODO:修改
        return CommonResponse.createForSuccess("查询上月额度成功",quota);
    }

}
