package com.example.cntsbackend.dto;

import java.util.List;

public class BlockInfoDto {
    private List<Integer> accounts;  // 账户数组
    private List<Double> coins;  // 余额数组
    private List<Double> remains;  // 额度数组

    public BlockInfoDto(List<Integer> accounts, List<Double> coins, List<Double> remains) {
        this.accounts = accounts;
        this.coins = coins;
        this.remains = remains;
    }

    public BlockInfoDto() {
    }

    public List<Integer> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Integer> accounts) {
        this.accounts = accounts;
    }

    public List<Double> getCoins() {
        return coins;
    }

    public void setCoins(List<Double> coins) {
        this.coins = coins;
    }

    public List<Double> getRemains() {
        return remains;
    }

    public void setRemains(List<Double> remains) {
        this.remains = remains;
    }
}
