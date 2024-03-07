package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Model2;
import com.example.cntsbackend.persistence.Model2Mapper;
import com.example.cntsbackend.service.Model2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class Model2ServiceImpl implements Model2Service {
    @Autowired
    private Model2Mapper model2Mapper;
    double[] REC1 = new double[21];//退役设备容量
    double[] REC2 = new double[21];//退役设备实际回收容量
    double[] REP1 = new double[21];//修理设备容量
    double[] REP2 = new double[21];//修理设备实际容量
    static int GWP = 23900;
    double ESF6,Ee,AD,EF,EL_gong,EL_shou,EL_internet,EL_in,EL_out;
    //相应区域电网排放因子还需查找

    //用户需要输入一系列退役设备的设备容量和实际回收容量、一系列修理设备的设备容量和实际回收容量、所在地区(东北、华北、华东、华中、西北、南方电网划分)、电厂上网电量（兆瓦时）、自外省输入电量（兆瓦时）、向外省输出电量（兆瓦时）、数据的月份、公司账户名
    public CommonResponse<Model2> CarbonAccounting() {
        for (int i = 0; i < REC1.length; i++) {
            ESF6 = ESF6 + REC1[i] - REC2[i];
        }
        for (int i = 0; i < REP1.length; i++) {
            ESF6 = ESF6 + REP1[i] - REP2[i];
        }
        ESF6 = ESF6 * GWP / 1000;

        EL_gong = EL_internet + EL_in - EL_out;
        AD = EL_gong - EL_shou;
        Ee = AD * EF;
        double E = ESF6+Ee;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatedDateTime = format.format(new Date());
        Date timestamp = Timestamp.valueOf(formatedDateTime);

        Model2 model2 = new Model2();//TODO:传进参数
        model2Mapper.insert(model2);
        return CommonResponse.createForSuccess("计算成功",model2);
    }

    @Override
    public CommonResponse<List<Model2>> getCarbonAccounting(String name) {
        List<Model2> model2List = model2Mapper.selectList(new QueryWrapper<Model2>().eq("account_name", name));
        return CommonResponse.createForSuccess("查询成功",model2List);
    }

    @Override
    public CommonResponse<List<Model2>> getAllCarbonAccounting() {
        List<Model2> model2List = model2Mapper.selectList(null);
        return CommonResponse.createForSuccess("查询成功",model2List);
    }

    @Override
    public CommonResponse<Model2> AuditingCarbonAccounting(int id, int state) {
        QueryWrapper<Model2> queryWrapper = new QueryWrapper<Model2>().eq("id", id);
        Model2 model2 = model2Mapper.selectOne(queryWrapper);
        model2.setState(state);
        UpdateWrapper<Model2> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        model2Mapper.update(model2, updateWrapper);
        return CommonResponse.createForSuccess("审核成功");
    }
}
