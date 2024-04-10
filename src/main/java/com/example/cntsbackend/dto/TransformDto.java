package com.example.cntsbackend.dto;


import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Data
public class TransformDto {
    private int account_id; // 卖家ID
    private int quotaSale_id; // 卖家售卖记录ID
    private double amount; // 购买的额度
    private String block_data; // 区块数据  "id,remain,coin;id1,remain1,coin1;..."


    public TransformDto(int account_id, int quotaSale_id, double amount, String block_data) {
        this.account_id = account_id;
        this.quotaSale_id = quotaSale_id;
        this.amount = amount;
        this.block_data = block_data;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public void setQuotaSale_id(int quotaSale_id) {
        this.quotaSale_id = quotaSale_id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBlock_data(String block_data) {
        this.block_data = block_data;
    }

    public List<BlockInfoDto> getBlockInfoList() {
        List<BlockInfoDto> blockInfoList = new ArrayList<>();
        String[] blockDataArray = block_data.split(";");
        for (String blockData : blockDataArray) {
            if (blockData.isEmpty()) {
                continue;
            }
            String[] blockDataArray1 = blockData.split(",");
            BlockInfoDto blockInfoDto = new BlockInfoDto(Integer.parseInt(blockDataArray1[0]), Double.parseDouble(blockDataArray1[1]), Double.parseDouble(blockDataArray1[2]));
            blockInfoList.add(blockInfoDto);
        }
        return blockInfoList;
    }
}
