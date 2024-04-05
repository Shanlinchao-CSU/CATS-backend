package com.example.cntsbackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class AccountingRecordDto {
    private int id;
    private int enterprise_id;
    private String month;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    private String state;
    private String variable_json;
    private String result;
    private int conductor_id;
    private String account_name;
    private String enterprise_type;
    private String public_key;

    public AccountingRecordDto(int id, int enterprise_id, String month, Date time, String state, String variable_json, String result, int conductor_id, String account_name, String enterprise_type) {
        this.id = id;
        this.enterprise_id = enterprise_id;
        this.month = month;
        this.time = time;
        this.state = state;
        this.variable_json = variable_json;
        this.result = result;
        this.conductor_id = conductor_id;
        this.account_name = account_name;
        this.enterprise_type = enterprise_type;
    }
    public AccountingRecordDto() {
    }

    public AccountingRecordDto(int id, int enterprise_id, String month, Date time, String s, String variable_json, String result, int conductor_id, String account_name, Object o, String public_key) {
        this.id = id;
        this.enterprise_id = enterprise_id;
        this.month = month;
        this.time = time;
        this.state = state;
        this.variable_json = variable_json;
        this.result = result;
        this.conductor_id = conductor_id;
        this.account_name = account_name;
        this.enterprise_type = enterprise_type;
        this.public_key = public_key;
    }
}
