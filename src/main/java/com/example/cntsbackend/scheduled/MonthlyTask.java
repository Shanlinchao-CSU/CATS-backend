package com.example.cntsbackend.scheduled;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.CMessage;
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
    private AccountService accountService;
    @Autowired
    private CMessageMapper cMessageMapper;

    @Scheduled(cron = "0 0 0 1 * *") // 每月1号零点执行
    public void executeTask() {
        // 在这里编写每月1号执行的任务逻辑
        // 获取当前时间的上个月份
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        // 格式化为"yyyy-MM"的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String previousMonthString = previousYearMonth.format(formatter);
        CommonResponse<List<Account>> allEnterpriseUsers = accountService.getAllEnterpriseUsers();
        List<Account> data = allEnterpriseUsers.getData();
        for (Account datum : data) {
            int account_id = datum.getAccount_id();
            cMessageMapper.insert(new CMessage(account_id, 500, previousMonthString, 500));
        }
    }
}
