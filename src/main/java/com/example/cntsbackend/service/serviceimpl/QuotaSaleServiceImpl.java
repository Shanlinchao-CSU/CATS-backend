package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.*;
import com.example.cntsbackend.dto.QuotaSaleDto;
import com.example.cntsbackend.persistence.*;
import com.example.cntsbackend.service.QuotaSaleService;
import com.example.cntsbackend.util.AES;
import com.example.cntsbackend.util.BigDecimalUtil;
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
    @Autowired
    private AccountingRecordMapper accountingRecordMapper;
    @Autowired
    private AccountLimitMapper accountLimitMapper;
    private static final String KEY = "2a34575d0f1b7cb39a2c117c0650311a4d3a6e4f507142b45cc3d144bd62ec41";

    public CommonResponse<Double> PublishTransaction(int account_id , double quota , double unit_price) {
        // 获取当前时间的上个月份
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        // 格式化为"yyyy-MM"的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String previousMonthString = previousYearMonth.format(formatter);
        //判断用户是否进行了上月的碳核算
        AccountingRecord accountingRecord = accountingRecordMapper.selectOne(new QueryWrapper<AccountingRecord>().eq("enterprise_id", account_id).eq("month", previousMonthString).eq("state", 0));
        if(accountingRecord != null){
            CMessage cMessage = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", account_id));
            double t_remain = cMessage.getT_remain();
            //获取还能出售的额度
            Double aDouble = GetRemain(account_id).getData();
            //当前还能出售的额度减去本次发布额度
            double subtract = BigDecimalUtil.subtract(aDouble, quota);
            if(subtract>=0){
                //数据库增添一行数据
                QuotaSale quotaSale = new QuotaSale(quota,account_id,unit_price,previousMonthString);
                quotaSaleMapper.insert(quotaSale);
                //CMessage更新数据
//                t_remain = BigDecimalUtil.subtract(t_remain,quota);
////                t_remain = t_remain - quota;
//                cMessage.setT_remain(t_remain);
//                UpdateWrapper<CMessage> updateWrapper = new UpdateWrapper<>();
//                updateWrapper.eq("account_id",account_id);
//                cMessageMapper.update(cMessage, updateWrapper);

                return CommonResponse.createForSuccess("发布交易信息成功",subtract);
            }
            return CommonResponse.createForError("发布交易信息失败，额度不足",aDouble);
        }else return CommonResponse.createForError("您未进行上月碳核算，发布交易信息失败");
    }

    public CommonResponse<String> cancelTransactionData(int id){
        QuotaSale quotaSale = quotaSaleMapper.selectOne(new QueryWrapper<QuotaSale>().eq("id", id));
        if(quotaSale != null){
//            double quota = quotaSale.getQuota();
//            int seller_id = quotaSale.getSeller_id();
//            CMessage cMessage = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", seller_id));
//            double t_remain = cMessage.getT_remain();
//            t_remain = BigDecimalUtil.add(t_remain,quota);
////        t_remain = t_remain + quota;
//            cMessage.setT_remain(t_remain);
//            UpdateWrapper<CMessage> updateWrapper = new UpdateWrapper<>();
//            updateWrapper.eq("account_id",seller_id);
//            cMessageMapper.update(cMessage, updateWrapper);
            quotaSaleMapper.deleteById(quotaSale);
            return CommonResponse.createForSuccess("取消发布交易信息成功");
        }else return CommonResponse.createForError("取消发布交易信息失败，该交易信息不存在");
    }

    public CommonResponse<String> ModifyUnitPrice(int id,double unit_price) {
        QuotaSale quotaSale = quotaSaleMapper.selectOne(new QueryWrapper<QuotaSale>().eq("id", id));
        if(quotaSale != null){
            quotaSale.setUnit_price(unit_price);
            UpdateWrapper<QuotaSale> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            quotaSaleMapper.update(quotaSale, updateWrapper);
            return CommonResponse.createForSuccess("修改单价成功");
        }else return CommonResponse.createForError("修改单价失败，该交易不存在");

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

    public CommonResponse<List<QuotaSaleDto>> getAllRemain(int account_id) throws Exception {
        // 获取当前时间的上个月份
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        // 格式化为"yyyy-MM"的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String previousMonthString = previousYearMonth.format(formatter);
        List<QuotaSale> quotaSaleList = quotaSaleMapper.selectList(new QueryWrapper<QuotaSale>().eq("month",previousMonthString).ne("seller_id",account_id));
        List<QuotaSaleDto> quotaSaleDtoList = new ArrayList<>();
        for (QuotaSale quotaSale : quotaSaleList) {
            int seller_id = quotaSale.getSeller_id();
            Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", seller_id));
            String public_key = account.getPublic_key();
            public_key = AES.decrypt(public_key,AES.hexToBytes(KEY));
            QuotaSaleDto quotaSaleDto = new QuotaSaleDto(quotaSale.getId(), quotaSale.getQuota(), seller_id, quotaSale.getUnit_price(), quotaSale.getMonth(), account.getAccount_name(),public_key);
            quotaSaleDtoList.add(quotaSaleDto);
        }
        return CommonResponse.createForSuccess("获取所有用户余额成功",quotaSaleDtoList);
    }


    public CommonResponse<Double> GetRemain(int account_id){
        // 获取当前时间的上个月份
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        // 格式化为"yyyy-MM"的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String previousMonthString = previousYearMonth.format(formatter);
        //判断用户是否进行了上月的碳核算
        AccountingRecord accountingRecord = accountingRecordMapper.selectOne(new QueryWrapper<AccountingRecord>().eq("enterprise_id", account_id).eq("month", previousMonthString).eq("state", 0));
        AccountLimit accountLimit = accountLimitMapper.selectOne(new QueryWrapper<AccountLimit>().eq("account_id", account_id));
        CMessage cMessage = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", account_id).eq("month", previousMonthString));
        if(accountingRecord == null){
            double t_limit = accountLimit.getT_limit();
            return CommonResponse.createForSuccess("获取用户的默认额度成功",t_limit);
        }else {
            double t_remain = cMessage.getT_remain();
            double allquota = 0;
            List<QuotaSale> quotaSaleList = quotaSaleMapper.selectList(new QueryWrapper<QuotaSale>().eq("seller_id", account_id).eq("month", previousMonthString));
            for (QuotaSale quotaSale : quotaSaleList) {
                double quota = quotaSale.getQuota();
                allquota += quota;
            }
            t_remain -= allquota;
            return CommonResponse.createForSuccess("获取用户还能出售额度成功",t_remain);
        }


    }
}
