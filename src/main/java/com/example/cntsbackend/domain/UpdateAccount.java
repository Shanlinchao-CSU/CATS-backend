package com.example.cntsbackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "update_account")
public class UpdateAccount {
    @TableId(type = IdType.AUTO)
    private int account_id;
    private String account_name;
    private String password;
    private String phone;
    private String email;
    private Integer type;
    private Integer enterprise_type;
    private int conductor_id;
    private String file;

    public UpdateAccount(int account_id,String account_name, Integer enterprise_type, String file) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.enterprise_type = enterprise_type;
        this.file = file;
    }

    public UpdateAccount() {
    }
}

