package com.example.cntsbackend.service.serviceimpl;

import com.example.cntsbackend.persistence.Model2Mapper;
import com.example.cntsbackend.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Model2ServiceImpl {
    double[] REC1 = new double[21];//退役设备容量
    double[] REC2 = new double[21];//退役设备实际回收容量
    double[] REP1 = new double[21];//修理设备容量
    double[] REP2 = new double[21];//修理设备实际容量
    static int GWP = 23900;
    double ESF6;
    double Ee;
    double AD;
    double EF;
    double EL_gong;
    double EL_shou;
    double EL_internet;
    double EL_in;
    double EL_out;


    //TODO:用户需要输入一系列退役设备的设备容量(double[]REC1)和实际回收容量(double[] REC2)、一系列修理设备的设备容量(double[] REP1)和实际回收容量(double[] REP2)、EF:区域电网年平均供电排放因子、EL_shou:售电量、EL_internet:电厂上网电量（兆瓦时）、EL_in:自外省输入电量（兆瓦时）、EL_out:向外省输出电量（兆瓦时）、数据的月份、公司账户名
    public double Model2CarbonAccounting(double[] REC1, double[] REC2, double[] REP1, double[] REP2, double EL_internet, double EL_in, double EL_out, double EL_shou, double EF) {

        double ESF6 = 0.0,Ee,AD,EL_gong;
        for (int i = 0; i < REC1.length; i++) {
            ESF6 = BigDecimalUtil.subtract(BigDecimalUtil.add(ESF6,REC1[i]),REC2[i]);
//            ESF6 = ESF6 + REC1[i] - REC2[i];
        }
        for (int i = 0; i < REP1.length; i++) {
            ESF6 = BigDecimalUtil.subtract(BigDecimalUtil.add(ESF6,REP1[i]),REP2[i]);
//            ESF6 = ESF6 + REP1[i] - REP2[i];
        }
        ESF6 = BigDecimalUtil.divide(BigDecimalUtil.multiply(ESF6,GWP),1000);
//        ESF6 = ESF6 * GWP / 1000;

        EL_gong = BigDecimalUtil.subtract(BigDecimalUtil.add(EL_internet,EL_in),EL_out);
//        EL_gong = EL_internet + EL_in - EL_out;
        AD = BigDecimalUtil.subtract(EL_gong,EL_shou);
//        AD = EL_gong - EL_shou;
        Ee = BigDecimalUtil.multiply(AD,EF);
//        Ee = AD * EF;
        return BigDecimalUtil.multiply(ESF6,Ee);
    }

}
