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
    private int state;
    private String variable_json;
    private String result;
    private int conductor_id;
    private String account_name;
    private String enterprise_type;
}
