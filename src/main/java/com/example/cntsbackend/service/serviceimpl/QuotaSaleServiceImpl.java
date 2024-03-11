package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.CMessage;
import com.example.cntsbackend.domain.QuotaSale;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.dto.QuotaSaleDto;
import com.example.cntsbackend.persistence.AccountMapper;
import com.example.cntsbackend.persistence.CMessageMapper;
import com.example.cntsbackend.persistence.QuotaSaleMapper;
import com.example.cntsbackend.service.QuotaSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            // 获取当前时间的上个月份
            YearMonth currentYearMonth = YearMonth.now();
            YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
            // 格式化为"yyyy-MM"的字符串
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            String previousMonthString = previousYearMonth.format(formatter);
            //数据库增添一行数据
            QuotaSale quotaSale = new QuotaSale(quota,account_id,unit_price,previousMonthString);
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

    public CommonResponse<List<QuotaSale>> getRemain(int account_id){
        // 获取当前时间的上个月份
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        // 格式化为"yyyy-MM"的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String previousMonthString = previousYearMonth.format(formatter);
        List<QuotaSale> quotaSaleList = quotaSaleMapper.selectList(new QueryWrapper<QuotaSale>().eq("seller_id", account_id).eq("month", previousMonthString));
        return CommonResponse.createForSuccess("查询上月额度成功",quotaSaleList);
    }

    public CommonResponse<List<QuotaSaleDto>> getAllRemain(){
        // 获取当前时间的上个月份
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        // 格式化为"yyyy-MM"的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String previousMonthString = previousYearMonth.format(formatter);
        List<QuotaSale> quotaSaleList = quotaSaleMapper.selectList(new QueryWrapper<QuotaSale>().eq("month",previousMonthString));
        List<QuotaSaleDto> quotaSaleDtoList = new ArrayList<>();
        for (int i = 0; i < quotaSaleList.size(); i++) {
            QuotaSaleDto quotaSaleWithAccount = quotaSaleMapper.getQuotaSaleWithAccount(quotaSaleList.get(i).getSeller_id());
            quotaSaleDtoList.add(quotaSaleWithAccount);
        }
        return CommonResponse.createForSuccess("获取所有用户余额成功",quotaSaleDtoList);
    }

}
