package com.example.cntsbackend.dto;

import java.util.List;

public class BlockInfoDto {
    private int account;  // 账户数组
    private double coin;  // 余额数组
    private double remain;  // 额度数组

    public BlockInfoDto(int account, double coin, double remain) {
        this.account = account;
        this.coin = coin;
        this.remain = remain;
    }

    public BlockInfoDto() {
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public double getCoin() {
        return coin;
    }

    public void setCoin(double coin) {
        this.coin = coin;
    }

    public double getRemain() {
        return remain;
    }

    public void setRemain(double remain) {
        this.remain = remain;
    }
}
