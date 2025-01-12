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
    private double t_limit;
    private String month;
    private double t_remain;
    private double limit_next_month;
    private String public_key;

    public AccountDto(int account_id, String account_name, String phone, String email, Integer type, String file, double t_coin, String enterprise_type, double t_limit, String public_key) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.phone = phone;
        this.email = email;
        this.type = type;
        this.file = file;
        this.t_coin = t_coin;
        this.enterprise_type = enterprise_type;
        this.t_limit = t_limit;
        this.public_key = public_key;
    }

    public AccountDto(int account_id, String account_name, String phone, String email, String enterprise_type, String month, double t_remain, double t_limit) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.phone = phone;
        this.email = email;
        this.enterprise_type = enterprise_type;
        this.month = month;
        this.t_remain = t_remain;
        this.t_limit = t_limit;
    }
    public AccountDto(int account_id, String account_name, String phone, String email, Integer type, String file, double t_coin, String cnType, double t_limit, double limit_next_month) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.phone = phone;
        this.email = email;
        this.type = type;
        this.file = file;
        this.t_coin = t_coin;
        this.enterprise_type = cnType;
        this.t_limit = t_limit;
        this.limit_next_month = limit_next_month;
    }

    public AccountDto() {
    }


    public AccountDto(int account_id, String account_name, String phone, String email, String cnType, double t_coin, double t_limit) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.phone = phone;
        this.email = email;
        this.t_coin = t_coin;
        this.enterprise_type = cnType;
        this.t_limit = t_limit;
    }
}
