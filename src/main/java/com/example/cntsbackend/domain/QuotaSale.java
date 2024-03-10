package com.example.cntsbackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "quota_sale")
public class QuotaSale {
    @TableId(type = IdType.AUTO)
    private int id;
    private double quota;
    private int seller_id;
    private double unit_price;
    private String month;

    public QuotaSale(double quota, int seller_id, double unit_price,String month) {
        this.quota = quota;
        this.seller_id = seller_id;
        this.unit_price = unit_price;
        this.month = month;
    }

    public QuotaSale() {
    }
}
