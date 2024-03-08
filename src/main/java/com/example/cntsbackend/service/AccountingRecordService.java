package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.AccountingRecord;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface AccountingRecordService {
    //管理员获取所有待审核的碳核算请求
    CommonResponse<List<AccountingRecord>> getAllCarbonAccountingForReview();
    //管理员获取证明材料文件流（未测试）
    ResponseEntity<InputStreamResource> getSupportingMaterial(int id) throws IOException;
}
