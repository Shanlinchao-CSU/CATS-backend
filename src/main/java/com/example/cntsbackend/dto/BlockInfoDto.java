package com.example.cntsbackend.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
public class BlockInfoDto {
    private int account;  // 账户
    private double coin;  // 余额
    private double remain;  // 额度

    public BlockInfoDto(int account, double coin, double remain) {
        this.account = account;
        this.coin = coin;
        this.remain = remain;
    }

    public BlockInfoDto() {
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public void setCoin(double coin) {
        this.coin = coin;
    }

    public void setRemain(double remain) {
        this.remain = remain;
    }
}
