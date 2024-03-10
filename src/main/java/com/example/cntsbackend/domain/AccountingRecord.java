package com.example.cntsbackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "accounting_record")
public class AccountingRecord {
    @TableId(type = IdType.AUTO)
    private int id;
    private int enterprise_id;
    private String month;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    private int state;
    private String variable_json;
    private String result;
    private String supportingMaterial;
    private int conductor_id;
}

