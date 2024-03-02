package com.example.cntsbackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "account")
public class Account {
    @TableId(type = IdType.AUTO)
    private int account_id;
    private String account_name;
    private String password;
    private String phone;
    private String email;
    private Integer type;
    private Integer enterprise_type;

    public Account(String account_name, String password, String phone, String email, Integer type, Integer enterprise_type) {
        this.account_name = account_name;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.type = type;
        this.enterprise_type = enterprise_type;
    }

    public Account() {
    }
}

