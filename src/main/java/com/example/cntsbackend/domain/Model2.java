package com.example.cntsbackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "model1")
public class Model2 {
    //电网企业碳核算模型
    @TableId(type = IdType.AUTO)
    private int id;
    private int account_name;
    private int month;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    private int state;
    private double tCO2;
    private double tCO2SiliconHexafluoride;
    private double tCO2e;

}
