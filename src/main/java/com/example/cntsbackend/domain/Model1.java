package com.example.cntsbackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "model1")
public class Model1 {
    //民航企业碳核算模型
    @TableId(type = IdType.AUTO)
    private int id;
    private int account_name;
    private int month;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    private int state;
    private int tCO2;
    private int tCO2ran;
    private int tCO2e;
    private int anthracite;
    private int bituminousCoal;
    private int brownCoal;
    private int cokingCoal;
    private int coke;
    private int crudeOil;
    private int fuelOil;
    private int gasoline;
    private int diesel;
    private int kerosene;
    private int aviationGasolineDomestic;
    private int aviationGasolineInternational;
    private int aviationKeroseneDomestic;
    private int aviationKeroseneInternational;
    private int liquefiedNaturalGas;
    private int liquefiedPetroleumGas;
    private int refineryDryGas;
    private int naphtha;
    private int petroleumCoke;
    private int otherPetroleumProducts;
    private int naturalGas;
    private int cokeOvenGas;
    private int otherGases;
    private int blendedFuelDomestic;
    private int blendedFuelInternational;
    private int electricity;
    private int heat;

}
