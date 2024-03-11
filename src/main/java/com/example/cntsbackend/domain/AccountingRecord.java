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
    private String supporting_material;
    private int conductor_id;

    public AccountingRecord(int enterprise_id, String month, Date time, int state, String variable_json, String result, String supporting_material, int conductor_id) {
        this.enterprise_id = enterprise_id;
        this.month = month;
        this.time = time;
        this.state = state;
        this.variable_json = variable_json;
        this.result = result;
        this.supporting_material = supporting_material;
        this.conductor_id = conductor_id;
    }

    public AccountingRecord() {
    }
}

