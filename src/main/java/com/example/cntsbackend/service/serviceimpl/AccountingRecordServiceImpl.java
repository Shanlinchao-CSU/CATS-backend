package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.AccountingRecord;
import com.example.cntsbackend.persistence.AccountingRecordMapper;
import com.example.cntsbackend.service.AccountingRecordService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class AccountingRecordServiceImpl implements AccountingRecordService {
    @Autowired
    private AccountingRecordMapper accountingRecordMapper;

    public CommonResponse<List<AccountingRecord>> getAllCarbonAccountingForReview() {
        List<AccountingRecord> accountingRecordList = accountingRecordMapper.selectList(new QueryWrapper<AccountingRecord>().eq("state", 1));
        return CommonResponse.createForSuccess("查询成功",accountingRecordList);
    }



    public ResponseEntity<InputStreamResource> getSupportingMaterial(int id) throws IOException {
        AccountingRecord accountingRecord = accountingRecordMapper.selectOne(new QueryWrapper<AccountingRecord>().eq("id", id));
        String supportingMaterial = accountingRecord.getSupportingMaterial();
        // 1. 将字符串内容写入临时文件
        File tempFile = File.createTempFile("temp", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(supportingMaterial);
        }

        // 2. 创建文件输入流
        FileInputStream fileInputStream = new FileInputStream(tempFile);

        // 3. 将文件输入流传递给前端
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=temp.txt");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(fileInputStream));
    }
}
