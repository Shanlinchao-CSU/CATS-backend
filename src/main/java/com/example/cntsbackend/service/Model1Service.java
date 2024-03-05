package com.example.cntsbackend.service;


import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Model1;

import java.util.List;

public interface Model1Service {
    //碳核算(参数列表还不确定)
    CommonResponse<Model1> CarbonAccounting();
    //企业查看碳核算结果
    CommonResponse<List<Model1>> getCarbonAccounting(String name);
    //第三方监管机构和管理员查看所有企业用户的碳核算数据
    CommonResponse<List<Model1>> getAllCarbonAccounting();
    //数据审核员核验企业数据与碳核算结果
    CommonResponse<Model1> AuditingCarbonAccounting(int id , int state);
}
