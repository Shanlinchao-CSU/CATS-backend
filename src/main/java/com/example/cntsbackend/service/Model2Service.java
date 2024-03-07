package com.example.cntsbackend.service;


import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Model2;

import java.util.List;

public interface Model2Service {
    //碳核算(参数列表还不确定)
    CommonResponse<Model2> CarbonAccounting();
    //企业查看碳核算结果
    CommonResponse<List<Model2>> getCarbonAccounting(String name);
    //第三方监管机构和管理员查看所有企业用户的碳核算数据
    CommonResponse<List<Model2>> getAllCarbonAccounting();
    //数据审核员核验企业数据与碳核算结果
    CommonResponse<Model2> AuditingCarbonAccounting(int id , int state);
}
