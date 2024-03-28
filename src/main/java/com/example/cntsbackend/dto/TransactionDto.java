package com.example.cntsbackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class TransactionDto {
    private int transaction_id;
    private double amount;
    private String sale_account_name;
    private String buyer_account_name;
    private double cost;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date complete_time;

    public TransactionDto(int transaction_id, double amount, String sale_account_name, String buyer_account_name, double cost, Date complete_time) {
        this.transaction_id = transaction_id;
        this.amount = amount;
        this.sale_account_name = sale_account_name;
        this.buyer_account_name = buyer_account_name;
        this.cost = cost;
        this.complete_time = complete_time;
    }

    public TransactionDto() {
    }
}
