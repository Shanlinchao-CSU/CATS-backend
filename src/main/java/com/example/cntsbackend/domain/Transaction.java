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
    private double amount;
    private int buyer_id;
    private int sale_id;
    private double cost;
    private String transaction_hash;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date complete_time;

    public Transaction(double amount, int buyer_id, int sale_id, double cost, Date complete_time) {
        this.amount = amount;
        this.buyer_id = buyer_id;
        this.sale_id = sale_id;
        this.cost = cost;
        this.complete_time = complete_time;
    }

    public Transaction() {
    }

}
