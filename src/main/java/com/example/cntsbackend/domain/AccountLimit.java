package com.example.cntsbackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "account_limit")
public class AccountLimit {
    @TableId(type = IdType.AUTO)
    private int account_id;
    private double t_limit;
    private double t_next_month;

    public AccountLimit(int account_id, double t_limit, double t_next_month) {
        this.account_id = account_id;
        this.t_limit = t_limit;
        this.t_next_month = t_next_month;
    }

    public AccountLimit() {
    }
}
