package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.AccountingRecord;
import com.example.cntsbackend.dto.AccountingRecordDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface AccountingRecordService {
    //TODO：（改动）管理员获取所有待审核的碳核算请求
    CommonResponse<List<AccountingRecordDTO>> getAllCarbonAccountingForReview();
    //TODO：（新增）管理员获取所有碳核算请求
    CommonResponse<List<AccountingRecordDTO>> getAllCarbonAccounting();
    //管理员获取证明材料文件流（未测试）
    ResponseEntity<InputStreamResource> getSupportingMaterial(int id) throws IOException;
    //TODO：（新增）用户提交碳核算
    CommonResponse<String> SubmitCarbonAccounting(int enterprise_id, String variable_json, String result, File file);
    //TODO：（新增）用户查看以往自己的碳核算记录
    CommonResponse<List<AccountingRecordDTO>> getMyCarbonAccounting(int enterprise_id);
    //数据审核员审核
//    CommonResponse<String> CarbonAccounting(int id,int account_id) throws JsonProcessingException;
}
