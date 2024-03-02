package com.example.cntsbackend.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "cmessage")
public class CMessage {

    private int account_id;
    private int climit;

    public CMessage(int account_id, int climit) {
        this.account_id = account_id;
        this.climit = climit;
    }

    public CMessage() {
    }
}
