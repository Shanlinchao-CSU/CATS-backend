package com.example.cntsbackend.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "model1")
public class Model1 {
    private int id;
    private int accountName;
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
