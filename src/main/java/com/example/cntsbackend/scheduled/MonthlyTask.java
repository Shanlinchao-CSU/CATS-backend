package com.example.cntsbackend.scheduled;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.AccountLimit;
import com.example.cntsbackend.domain.CMessage;
import com.example.cntsbackend.persistence.AccountLimitMapper;
import com.example.cntsbackend.persistence.CMessageMapper;
import com.example.cntsbackend.service.AccountService;
import com.example.cntsbackend.service.serviceimpl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Component
public class MonthlyTask {
    @Autowired
    private AccountLimitMapper accountLimitMapper;

    @Scheduled(cron = "0 0 0 1 * *") // 每月1号零点执行
    public void executeTask() {
        List<AccountLimit> accountLimits = accountLimitMapper.selectList(null);
        for (AccountLimit accountLimit : accountLimits) {
            int account_id = accountLimit.getAccount_id();
            double t_next_month = accountLimit.getLimit_next_month();
            accountLimit.setT_limit(t_next_month);
            UpdateWrapper<AccountLimit> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("account_id", account_id);
            accountLimitMapper.update(accountLimit, updateWrapper);
        }
    }
}
