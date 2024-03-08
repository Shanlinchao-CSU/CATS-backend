package com.example.cntsbackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "transaction")
public class Transaction {
    @TableId(type = IdType.AUTO)
    private int transaction_id;
    private String t_from;
    private String t_to;
    private double amount;
    private double unit_price;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date start_time;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date complete_time;
    private Boolean state;

    public Transaction(String t_from, String t_to, double amount, double unit_price, Date start_time, Date complete_time, Boolean state) {
        this.t_from = t_from;
        this.t_to = t_to;
        this.amount = amount;
        this.unit_price = unit_price;
        this.start_time = start_time;
        this.complete_time = complete_time;
        this.state = state;
    }

    public Transaction() {
    }
}
