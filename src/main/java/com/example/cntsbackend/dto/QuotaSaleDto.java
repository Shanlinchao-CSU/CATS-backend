package com.example.cntsbackend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.function.LongToIntFunction;
@Data
public class QuotaSaleDto {
    private double quota;
    private int seller_id;
    private double unit_price;
    private String month;
    private String account_name;
}
