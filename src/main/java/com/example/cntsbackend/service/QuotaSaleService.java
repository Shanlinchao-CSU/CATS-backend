package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.QuotaSale;
import com.example.cntsbackend.dto.QuotaSaleDto;

import java.util.List;

public interface QuotaSaleService {
    //企业发布交易信息
    CommonResponse<String> PublishTransaction(int account_id , double quota , double unit_price);
    //企业取消已发布的交易信息(id为发布信息的id)
    CommonResponse<String> cancelTransactionData(int id);
    //企业修改单价(id为发布信息的id)
    CommonResponse<String> ModifyUnitPrice(int id,double unit_price);
    //企业查看上月额度剩余（未卖出）
    CommonResponse<List<QuotaSale>> getRemain(int account_id);
    //获取所有企业上月额度剩余（用于购买展示）
    CommonResponse<List<QuotaSaleDto>> getAllRemain();
}
