package com.example.cntsbackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.File;

@Data
@TableName(value = "register_application")
public class RegisterApplication {
    @TableId(type = IdType.AUTO)
    private int register_application_id;
    private String account_name;
    private String password;
    private String phone;
    private String email;
    private Integer type;
    private String file_address;
    private Integer enterprise_type;
    private int conductor_id;
    private Integer state;

    public RegisterApplication(String account_name, String password, String phone, Integer type, String file_address, Integer enterprise_type) {
        this.account_name = account_name;
        this.password = password;
        this.phone = phone;
        this.type = type;
        this.file_address = file_address;
        this.enterprise_type = enterprise_type;
        this.conductor_id = 0;
        this.state = 0;
    }

    public RegisterApplication() {
    }
}
