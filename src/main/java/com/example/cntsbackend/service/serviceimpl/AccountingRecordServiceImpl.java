package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.AccountingRecord;
import com.example.cntsbackend.dto.AccountingRecordDto;
import com.example.cntsbackend.persistence.AccountMapper;
import com.example.cntsbackend.persistence.AccountingRecordMapper;
import com.example.cntsbackend.service.AccountingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AccountingRecordServiceImpl implements AccountingRecordService {
    @Autowired
    private AccountingRecordMapper accountingRecordMapper;
    @Autowired
    private AccountMapper accountMapper;

    public CommonResponse<List<AccountingRecordDto>> getAllCarbonAccountingForReview() {
        List<AccountingRecord> accountingRecordList = accountingRecordMapper.selectList(new QueryWrapper<AccountingRecord>().eq("state", 1));
        List<AccountingRecordDto> accountingRecordDTOList = new ArrayList<>();
        for (AccountingRecord accountingRecord : accountingRecordList) {
            AccountingRecordDto accountingRecordsWithAccountName = accountingRecordMapper.getAccountingRecordsWithAccountName(accountingRecord.getEnterprise_id());
            String enterprise_type = accountingRecordsWithAccountName.getEnterprise_type();
            switch (enterprise_type) {
                case "1" -> accountingRecordsWithAccountName.setEnterprise_type("发电企业");
                case "2" -> accountingRecordsWithAccountName.setEnterprise_type("电网企业");
                case "3" -> accountingRecordsWithAccountName.setEnterprise_type("钢铁生产企业");
                case "4" -> accountingRecordsWithAccountName.setEnterprise_type("化工生产企业");
                case "5" -> accountingRecordsWithAccountName.setEnterprise_type("电解铝生产企业企业");
                case "6" -> accountingRecordsWithAccountName.setEnterprise_type("镁冶炼企业");
                case "7" -> accountingRecordsWithAccountName.setEnterprise_type("平板玻璃生产企业");
                case "8" -> accountingRecordsWithAccountName.setEnterprise_type("水泥生产企业");
                case "9" -> accountingRecordsWithAccountName.setEnterprise_type("陶瓷生产企业");
                case "10" -> accountingRecordsWithAccountName.setEnterprise_type("民航企业");
            }
            accountingRecordDTOList.add(accountingRecordsWithAccountName);
        }
        return CommonResponse.createForSuccess("获取所有待审核的碳核算请求成功",accountingRecordDTOList);
    }

    public CommonResponse<List<AccountingRecordDto>> getAllCarbonAccounting(){
        List<AccountingRecord> accountingRecordList = accountingRecordMapper.selectList(null);
        List<AccountingRecordDto> accountingRecordDTOList = new ArrayList<>();
        for (AccountingRecord accountingRecord : accountingRecordList) {
            AccountingRecordDto accountingRecordsWithAccountName = accountingRecordMapper.getAccountingRecordsWithAccountName(accountingRecord.getEnterprise_id());
            String enterprise_type = accountingRecordsWithAccountName.getEnterprise_type();
            switch (enterprise_type) {
                case "1" -> accountingRecordsWithAccountName.setEnterprise_type("发电企业");
                case "2" -> accountingRecordsWithAccountName.setEnterprise_type("电网企业");
                case "3" -> accountingRecordsWithAccountName.setEnterprise_type("钢铁生产企业");
                case "4" -> accountingRecordsWithAccountName.setEnterprise_type("化工生产企业");
                case "5" -> accountingRecordsWithAccountName.setEnterprise_type("电解铝生产企业企业");
                case "6" -> accountingRecordsWithAccountName.setEnterprise_type("镁冶炼企业");
                case "7" -> accountingRecordsWithAccountName.setEnterprise_type("平板玻璃生产企业");
                case "8" -> accountingRecordsWithAccountName.setEnterprise_type("水泥生产企业");
                case "9" -> accountingRecordsWithAccountName.setEnterprise_type("陶瓷生产企业");
                case "10" -> accountingRecordsWithAccountName.setEnterprise_type("民航企业");
            }
            accountingRecordDTOList.add(accountingRecordsWithAccountName);
        }
        return CommonResponse.createForSuccess("获取所有碳核算请求成功",accountingRecordDTOList);
    }



    public ResponseEntity<InputStreamResource> getSupportingMaterial(int id) throws IOException {
        AccountingRecord accountingRecord = accountingRecordMapper.selectOne(new QueryWrapper<AccountingRecord>().eq("id", id));
        String supportingMaterial = accountingRecord.getSupporting_material();
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

//    public CommonResponse<String> CarbonAccounting(int id,int account_id) throws JsonProcessingException {
//        AccountingRecord accountingRecord = accountingRecordMapper.selectOne(new QueryWrapper<AccountingRecord>().eq("id", id));
//        int state = accountingRecord.getState();
//        if(state != 1){
//            return CommonResponse.createForError("该请求已被处理");
//        }else{
//            int enterprise_id = accountingRecord.getEnterprise_id();
//            String variable_json = accountingRecord.getVariable_json();
//            String result = accountingRecord.getResult();
//            //将字符串解析为 JSON 对象
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.readTree(variable_json);
//
//            Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", enterprise_id));
//            Integer enterprise_type = account.getEnterprise_type();
//            switch (enterprise_type) {
//                case 1 :
//                    break;
////                case 2 -> {
////                    break;
////                }
//                case 3 :
//                case 4 :
//                case 5 :
//                case 6 :
//                case 7 :
//                case 8 :
//                case 9 :break;
////                case 10 -> {
////
////                }
//            }
//        }
//    }

    public CommonResponse<String> SubmitCarbonAccounting(int enterprise_id,String variable_json,String result,File file){
        // 获取当前时间的上个月份
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        // 格式化为"yyyy-MM"的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String previousMonthString = previousYearMonth.format(formatter);
        //获取当前时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatedDateTime = format.format(new Date());
        Date timestamp = Timestamp.valueOf(formatedDateTime);
        //获取文件
        String str= String.valueOf(file);
        AccountingRecord accountingRecord = new AccountingRecord(enterprise_id,previousMonthString,timestamp,1,variable_json,result,str,0);
        accountingRecordMapper.insert(accountingRecord);
        return CommonResponse.createForSuccess("用户提交碳核算请求成功");
    }

    public CommonResponse<List<AccountingRecordDto>> getMyCarbonAccounting(int enterprise_id){
        List<AccountingRecord> accountingRecordList = accountingRecordMapper.selectList(new QueryWrapper<AccountingRecord>().eq("enterprise_id", enterprise_id));
        List<AccountingRecordDto> accountingRecordDTOList = new ArrayList<>();
        for (int i = 0; i < accountingRecordList.size(); i++) {
            AccountingRecordDto accountingRecordsWithAccountName = accountingRecordMapper.getAccountingRecordsWithAccountName(accountingRecordList.get(i).getEnterprise_id());
            String enterprise_type = accountingRecordsWithAccountName.getEnterprise_type();
            switch (enterprise_type) {
                case "1" -> accountingRecordsWithAccountName.setEnterprise_type("发电企业");
                case "2" -> accountingRecordsWithAccountName.setEnterprise_type("电网企业");
                case "3" -> accountingRecordsWithAccountName.setEnterprise_type("钢铁生产企业");
                case "4" -> accountingRecordsWithAccountName.setEnterprise_type("化工生产企业");
                case "5" -> accountingRecordsWithAccountName.setEnterprise_type("电解铝生产企业企业");
                case "6" -> accountingRecordsWithAccountName.setEnterprise_type("镁冶炼企业");
                case "7" -> accountingRecordsWithAccountName.setEnterprise_type("平板玻璃生产企业");
                case "8" -> accountingRecordsWithAccountName.setEnterprise_type("水泥生产企业");
                case "9" -> accountingRecordsWithAccountName.setEnterprise_type("陶瓷生产企业");
                case "10" -> accountingRecordsWithAccountName.setEnterprise_type("民航企业");
            }
            accountingRecordDTOList.add(accountingRecordsWithAccountName);
        }
        return CommonResponse.createForSuccess(accountingRecordDTOList);
    }
    public CommonResponse<String> CancelMyCarbonAccounting(int id){
        AccountingRecord accountingRecord = accountingRecordMapper.selectOne(new QueryWrapper<AccountingRecord>().eq("id", id));
        if(accountingRecord != null){
            int state = accountingRecord.getState();
            if(state == 1){
                accountingRecordMapper.deleteById(id);
                return CommonResponse.createForSuccess("取消碳核算记录成功");
            }else return CommonResponse.createForError("该碳核算记录已被处理,取消失败");
        }else return CommonResponse.createForError("该碳核算记录不存在");
    }

    public CommonResponse<String> ModifyMyCarbonAccounting(int id,AccountingRecord accountingRecord){
        AccountingRecord accountingRecord1 = accountingRecordMapper.selectOne(new QueryWrapper<AccountingRecord>().eq("id", id));
        int state = accountingRecord1.getState();
        if(state ==1){
            accountingRecord1.setMonth(accountingRecord.getMonth());
            accountingRecord1.setResult(accountingRecord.getResult());
            accountingRecord1.setSupporting_material(accountingRecord.getSupporting_material());
            accountingRecord1.setVariable_json(accountingRecord.getVariable_json());
            //获取当前时间
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatedDateTime = format.format(new Date());
            Date timestamp = Timestamp.valueOf(formatedDateTime);
            accountingRecord1.setTime(timestamp);
            UpdateWrapper<AccountingRecord> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id);
            accountingRecordMapper.update(accountingRecord1,updateWrapper);
            return CommonResponse.createForSuccess("修改碳核算记录成功");
        }else return CommonResponse.createForError("该碳核算记录已被处理,修改失败");
    }

}
