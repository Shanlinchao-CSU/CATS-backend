package com.example.cntsbackend.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "cmessage")
public class CMessage {
    @TableId(type = IdType.AUTO)
    private int account_id;
    private double t_remain;
    private String month;
    private double t_limit;

    public CMessage(int account_id, double t_remain, String month, double limit) {
        this.account_id = account_id;
        this.t_remain = t_remain;
        this.month = month;
        this.t_limit = limit;
    }

    public CMessage() {
    }
}
