package com.example.cntsbackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class TransactionDto {
    private double amount;
    private String sale_account_name;
    private String buyer_account_name;
    private double cost;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date complete_time;
}
