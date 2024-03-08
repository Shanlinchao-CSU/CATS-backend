package com.example.cntsbackend.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "cmessage")
public class CMessage {

    private int account_id;
    private double t_remain;
    private double t_coin;
    public CMessage(int account_id, double t_remain) {
        this.account_id = account_id;
        this.t_remain = t_remain;
    }

    public CMessage() {
    }
}
