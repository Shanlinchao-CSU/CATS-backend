package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.AccountingRecord;
import com.example.cntsbackend.dto.AccountingRecordDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface AccountingRecordService {
    //管理员获取所有待审核的碳核算请求
    CommonResponse<List<AccountingRecordDto>> getAllCarbonAccountingForReview() throws Exception;
    //管理员获取所有碳核算请求
    CommonResponse<List<AccountingRecordDto>> getAllCarbonAccounting();
    //(id为核算记录的id)管理员获取证明材料文件流
    void getSupportingMaterial(int id, HttpServletResponse response) throws Exception;
    //用户提交碳核算
    CommonResponse<String> SubmitCarbonAccounting(int enterprise_id, String variable_json, String result, File file);
    //用户查看以往自己的碳核算记录
    CommonResponse<List<AccountingRecordDto>> getMyCarbonAccounting(int enterprise_id);
    //(参数为碳核算id)数据审核员审核
    CommonResponse<Double> CarbonAccounting(int id) throws JsonProcessingException;

    //(id为碳核算记录的id)用户取消碳核算记录(未被审核)
    CommonResponse<String> CancelMyCarbonAccounting(int id);
    //(id为碳核算记录的id)用户修改碳核算记录(未被审核)
    CommonResponse<String> ModifyMyCarbonAccounting(int id, AccountingRecord accountingRecord);
    //数据审核员获取自己审核的所有碳核算申请
    CommonResponse<List<AccountingRecordDto>> DataAuditorsGetMyCarbonAccounting(int account_id);
    //数据审核员处理碳核算请求
    CommonResponse<String> CarbonAccountingRequests(int id,boolean approve,int conductor_id);
}
