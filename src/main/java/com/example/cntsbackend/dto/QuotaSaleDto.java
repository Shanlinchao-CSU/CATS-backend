package com.example.cntsbackend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.function.LongToIntFunction;
@Data
public class QuotaSaleDto {
    private int id;
    private double quota;
    private int seller_id;
    private double unit_price;
    private String month;
    private String account_name;
    private String public_key;

    public QuotaSaleDto(int id, double quota, int seller_id, double unit_price, String month, String account_name,String public_key) {
        this.id = id;
        this.quota = quota;
        this.seller_id = seller_id;
        this.unit_price = unit_price;
        this.month = month;
        this.account_name = account_name;
        this.public_key = public_key;
    }

    public QuotaSaleDto() {
    }
}
