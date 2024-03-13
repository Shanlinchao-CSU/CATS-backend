package com.example.cntsbackend.dto;

import lombok.Data;

@Data
public class AccountDto {
    private int account_id;
    private String account_name;
    private String password;
    private String phone;
    private String email;
    private Integer type;
    private String file;
    private double t_coin;
    private String enterprise_type;

    public AccountDto(String account_name, String password, String phone, String email, Integer type, String file, double t_coin, String enterprise_type) {
        this.account_name = account_name;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.type = type;
        this.file = file;
        this.t_coin = t_coin;
        this.enterprise_type = enterprise_type;
    }

    public AccountDto() {
    }
}
