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
    private int registerApplication_id;
    private String account_name;
    private String password;
    private String phone;
    private String email;
    private Integer type;
    private String file_address;

    public RegisterApplication(String str, String name, String password, String phone, String email, int enterprise_type, int type) {
    }
}
