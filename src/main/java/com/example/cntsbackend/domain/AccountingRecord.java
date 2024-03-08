package com.example.cntsbackend.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "accounting_record")
public class AccountingRecord {
    private int id;
    private int enterpriseName;
    private int month;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    private int state;
    private String variableJson;
    private String result;
    private String supportingMaterial;
}

