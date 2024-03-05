package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.Model1;
import com.example.cntsbackend.domain.UpdateAccount;
import com.example.cntsbackend.persistence.Model1Mapper;
import com.example.cntsbackend.service.Model1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class Model1ServiceImpl implements Model1Service {
    double[] AD1 = new double[21];//化石燃料
    double[] AD2 = new double[21];//生物质混合燃料
    double[] FC1 = new double[21];//消耗量，用户输入
    double[] FC2 = new double[21];//消耗量，用户输入
    double[] NCV1 = {23210,22350,14080,17460,28435,41816,41816,43070,42652,43070,44300,44100,41868,50179,45998,44500,32500,40200,38931,17406,15758.4};
    double[] NCV2 = new double[21];//生物质混合燃料的低位发热值,用户输入
    double[] BF = new double[21];//生物质混合燃料中生物质含量,用户输入
    double[] EF = new double[21];//化石燃料的排放因子
    double[] CC = {27.4,26.1,28.0,33.6,29.5,20.1,21.1,18.9,20.2,19.6,19.1,19.5,17.2,17.2,18.2,20.0,27.5,20.0,15.3,13.6,12.2};//单位热值含碳量
    double[] OF = {0.94,0.93,0.96,0.90,0.93,0.98,0.98,0.98,0.98,0.98,1,1,0.98,0.98,0.98,0.98,0.98,0.98,0.99,0.99,0.99};
    double Eran,EE ,Ere;
    @Autowired
    Model1Mapper model1Mapper;
    //用户需要输入各燃料的消耗值，生物质混合燃料的消耗值、低位发热值、生物质含量，国内与国际化石燃料消耗量，国内与国际消耗的生物质混合燃料的消耗量、低位发热值以及生物质混合燃料中生物质的含量、数据的月份、公司账户名
    public CommonResponse<Model1> CarbonAccounting(){
        for (int i = 0; i < AD1.length; i++) {
            if(i == 12 || i == 13 || i == 14 ||i == 18|| i == 19 || i == 20){
                AD1[i] = FC1[i] * NCV1[i] / 10 / 1000000;//气体单位转换
            }
            AD1[i] = FC1[i] * NCV1[i] / 1000000;
            AD2[i] = FC2[i] * NCV2[i] / 1000000 * (1-BF[i]);//用户输入的生物质混合燃料单位要统一,否则还要转换
            EF[i] = CC[i] * OF[i] * 44 / 12;
            Eran = Eran + AD1[i] * EF[i] + AD2[i] * EF[i];
        }
        EE = ADdian * 0.5703;//ADdian、ADre均为前端传值
        Ere = ADre * 0.11;
        //总量 Eran+EE+Ere
        double E = Eran+EE+Ere;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatedDateTime = format.format(new Date());
        Date timestamp = Timestamp.valueOf(formatedDateTime);

        Model1 model1 = new Model1();//TODO:传进参数
        model1Mapper.insert(model1);
        return CommonResponse.createForSuccess("计算成功",model1);
    }

    public CommonResponse<List<Model1>> getCarbonAccounting(String name){
        List<Model1> model1List = model1Mapper.selectList(new QueryWrapper<Model1>().eq("account_name", name));
        return CommonResponse.createForSuccess("查询成功",model1List);
    }

    public CommonResponse<List<Model1>> getAllCarbonAccounting(){
        List<Model1> model1List = model1Mapper.selectList(null);
        return CommonResponse.createForSuccess("查询成功",model1List);
    }

    public CommonResponse<Model1> AuditingCarbonAccounting(int id , int state){
        QueryWrapper<Model1> queryWrapper = new QueryWrapper<Model1>().eq("id", id);
        Model1 model1 = model1Mapper.selectOne(queryWrapper);
        model1.setState(state);
        UpdateWrapper<Model1> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        model1Mapper.update(model1, updateWrapper);
        return CommonResponse.createForSuccess("审核成功");
    }

}
