package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Model1;
import com.example.cntsbackend.persistence.Model1Mapper;
import com.example.cntsbackend.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Model1ServiceImpl {
    static double[] AD1 = new double[23];//化石燃料
    static double[] AD2 = new double[23];//生物质混合燃料
    double[] FC1 = new double[23];//化石燃料消耗量，用户输入。固体或液体燃料以t为单位，对气体燃料以 103m3为单位；
    double[] FC2 = new double[23];//生物质混合燃料消耗量，用户输入
    static double[] NCV1 = {23210,22350,14080,17460,28435,41816,41816,43070,42652,43070,44300,44300,44100,44100,41868,50179,45998,44500,32500,40200,38931,17406,15758.4};
    double[] NCV2 = new double[23];//生物质混合燃料的低位发热值,用户输入
    double[] BF = new double[23];//生物质混合燃料中生物质含量,用户输入
    static double[] EF = new double[23];//化石燃料的排放因子
    static double[] CC = {27.4,26.1,28.0,33.6,29.5,20.1,21.1,18.9,20.2,19.6,19.1,19.1,19.5,19.5,17.2,17.2,18.2,20.0,27.5,20.0,15.3,13.6,12.2};//单位热值含碳量
    static double[] OF = {0.94,0.93,0.96,0.90,0.93,0.98,0.98,0.98,0.98,0.98,1,1,1,1,0.98,0.98,0.98,0.98,0.98,0.98,0.99,0.99,0.99};
    static double Eran;
    static double EE;
    static double Ere;
    double ADE;
    double EFE;
    double ADre;//ADE:企业的净购入电量（MWh）,EFE:区域电网年平均供电排放因子（tCO2/MWh）,ADre:企业净购入的热力（GJ）
    @Autowired
    Model1Mapper model1Mapper;
    //todo:用户需要输入各燃料的消耗值，生物质混合燃料的消耗值、低位发热值、生物质含量，国内与国际化石燃料消耗量(double[] FC3)，国内与国际消耗的生物质混合燃料的消耗量(double[] FC4)、低位发热值以及生物质混合燃料中生物质的含量、数据的月份、公司账户名、企业的净购入电量（MWh）、区域电网年平均供电排放因子（tCO2/MWh）、企业净购入的热力（GJ）
    public static double Model1CarbonAccounting(double[] FC1,double[] FC2,double[] NCV2,double[] BF,double ADE,double EFE,double ADre){
        for (int i = 0; i < AD1.length; i++) {
            AD1[i] = BigDecimalUtil.divide(BigDecimalUtil.multiply(FC1[i],NCV1[i]),1000000);
//            AD1[i] = FC1[i] * NCV1[i]  / 1000000;
            AD2[i] = BigDecimalUtil.multiply(BigDecimalUtil.divide(BigDecimalUtil.multiply(FC2[i],NCV2[i]),1000000),BigDecimalUtil.subtract(1,BF[i]));
//            AD2[i] = FC2[i] * NCV2[i]  / 1000000 * (1-BF[i]);//用户输入的生物质混合燃料单位要统一,否则还要转换
            EF[i] = BigDecimalUtil.divide(BigDecimalUtil.multiply(BigDecimalUtil.multiply(CC[i],OF[i]),44),12);
//            EF[i] = CC[i] * OF[i] * 44 / 12;
            Eran = BigDecimalUtil.add(BigDecimalUtil.add(Eran,BigDecimalUtil.multiply(AD1[i],EF[i])),BigDecimalUtil.multiply(AD2[i],0.3));
//            Eran = Eran + AD1[i] * EF[i] + AD2[i] * 0.3;
        }
        EE = BigDecimalUtil.multiply(ADE,EFE);
//        EE = ADE * EFE;
        Ere = BigDecimalUtil.multiply(ADre,0.11);
//        Ere = ADre * 0.11;
        //总量 Eran+EE+Ere
        return BigDecimalUtil.add(BigDecimalUtil.add(Eran,EE),Ere);
    }
}
