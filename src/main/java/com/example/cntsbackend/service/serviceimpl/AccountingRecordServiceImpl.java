package com.example.cntsbackend.service.serviceimpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.common.EnterpriseType;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.AccountingRecord;
import com.example.cntsbackend.dto.AccountingRecordDto;
import com.example.cntsbackend.persistence.AccountLimitMapper;
import com.example.cntsbackend.persistence.AccountMapper;
import com.example.cntsbackend.persistence.AccountingRecordMapper;
import com.example.cntsbackend.persistence.CMessageMapper;
import com.example.cntsbackend.service.AccountingRecordService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CMessageMapper cMessageMapper;
    @Autowired
    private AccountLimitMapper accountLimitMapper;

    public CommonResponse<List<AccountingRecordDto>> getAllCarbonAccountingForReview() {
        List<AccountingRecord> accountingRecordList = accountingRecordMapper.selectList(new QueryWrapper<AccountingRecord>().eq("state", 1));
        List<AccountingRecordDto> accountingRecordDTOList = new ArrayList<>();
        for (AccountingRecord accountingRecord : accountingRecordList) {
            int enterprise_id = accountingRecord.getEnterprise_id();
            Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", enterprise_id));
            String account_name = account.getAccount_name();
            Integer enterprise_type = account.getEnterprise_type();
            String s = "待审核";
            AccountingRecordDto accountingRecordDto = new AccountingRecordDto(accountingRecord.getId(),accountingRecord.getEnterprise_id(),accountingRecord.getMonth(),accountingRecord.getTime(), s,accountingRecord.getVariable_json(),accountingRecord.getResult(),accountingRecord.getConductor_id(),account_name,null);
            EnterpriseType.setDto(accountingRecordDto, enterprise_type);
            accountingRecordDTOList.add(accountingRecordDto);
        }
        return CommonResponse.createForSuccess("获取所有待审核的碳核算请求成功",accountingRecordDTOList);
    }

    public CommonResponse<List<AccountingRecordDto>> getAllCarbonAccounting(){
        List<AccountingRecord> accountingRecordList = accountingRecordMapper.selectList(null);
        List<AccountingRecordDto> accountingRecordDTOList = new ArrayList<>();
        for (AccountingRecord accountingRecord : accountingRecordList) {
            int enterprise_id = accountingRecord.getEnterprise_id();
            Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", enterprise_id));
            String account_name = account.getAccount_name();
            Integer enterprise_type = account.getEnterprise_type();
            String s = String.valueOf(accountingRecord.getState());
            if(s.equals("0")){
                s = "通过";
            }else if(s.equals("2")){
                s = "拒绝";
            }else s = "待审核";
            AccountingRecordDto accountingRecordDto = new AccountingRecordDto(accountingRecord.getId(),accountingRecord.getEnterprise_id(),accountingRecord.getMonth(),accountingRecord.getTime(),s,accountingRecord.getVariable_json(),accountingRecord.getResult(),accountingRecord.getConductor_id(),account_name,null);
            EnterpriseType.setDto(accountingRecordDto, enterprise_type);
            accountingRecordDTOList.add(accountingRecordDto);
        }
        return CommonResponse.createForSuccess("获取所有碳核算请求成功",accountingRecordDTOList);
    }



    public void getSupportingMaterial(int id, HttpServletResponse response) throws Exception {
        AccountingRecord accountingRecord = accountingRecordMapper.selectOne(new QueryWrapper<AccountingRecord>().eq("id", id));
        String supportingMaterial = accountingRecord.getSupporting_material();
        int enterprise_id = accountingRecord.getEnterprise_id();
        String month = accountingRecord.getMonth();
        String fileName = enterprise_id+"_"+month;
        try (FileInputStream inputStream = new FileInputStream(supportingMaterial);
             OutputStream outputStream = response.getOutputStream()) {
            byte[] data = new byte[9216];
            // 全文件类型（传什么文件返回什么文件流）
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setHeader("Accept-Ranges", "bytes");
            int read;
            while ((read = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, read);
            }
            // 将缓存区数据进行输出
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("失败"+e);
        }
    }

//    public CommonResponse<String> CarbonAccounting(int id,int account_id){
//        AccountingRecord accountingRecord = accountingRecordMapper.selectOne(new QueryWrapper<AccountingRecord>().eq("id", id));
//        int state = accountingRecord.getState();
//        if(state != 1){
//            return CommonResponse.createForError("该请求已被处理");
//        }else{
//            //得到企业id
//            int enterprise_id = accountingRecord.getEnterprise_id();
//            //得到证明材料的字符串
//            String variable_json = accountingRecord.getVariable_json();
//            //得到用户提交的结果
//            String result = accountingRecord.getResult();
//            double result_double = Double.parseDouble(result);
//            //将字符串解析为 JSON 对象
//            JSONObject jsonObject=JSONObject.parseObject(variable_json);
//
//            Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", enterprise_id));
//            AccountLimit accountLimit = accountLimitMapper.selectOne(new QueryWrapper<AccountLimit>().eq("account_id", enterprise_id));
//            double t_limit = accountLimit.getT_limit();
//            Integer enterprise_type = account.getEnterprise_type();
//            switch (enterprise_type) {
//                case 1 :
//                    break;
//                case 2 :
//                    JSONArray rec_cap_arr = jsonObject.getJSONArray("rec_cap_arr");
//                    JSONArray rec_pra_arr = jsonObject.getJSONArray("rec_pra_arr");
//                    JSONArray rep_cap_arr = jsonObject.getJSONArray("rep_cap_arr");
//                    JSONArray rep_pra_arr = jsonObject.getJSONArray("rep_pra_arr");
//                    double[] rec_cap_arr1 = jsonArrayToDoubleArray(rec_cap_arr);
//                    double[] rec_pra_arr1 = jsonArrayToDoubleArray(rec_pra_arr);
//                    double[] rep_cap_arr1 = jsonArrayToDoubleArray(rep_cap_arr);
//                    double[] rep_pra_arr1 = jsonArrayToDoubleArray(rep_pra_arr);
//                    double ef = jsonObject.getIntValue("ef");//区域排放因子
//                    int rec_num = jsonObject.getIntValue("rec_num");//退役设备数量
//                    int rep_num = jsonObject.getIntValue("rep_num");//修理设备数量
//                    double el_net = jsonObject.getIntValue("el_net");//电网上网数量
//                    double el_input = jsonObject.getIntValue("el_input");//自外省输入电量
//                    double el_output = jsonObject.getIntValue("el_output");//自外省输出电量
//                    double el_sell = jsonObject.getIntValue("el_sell");//售电量
//                    double res = Model2ServiceImpl.Model2CarbonAccounting(rec_cap_arr1, rec_pra_arr1, rep_cap_arr1, rep_pra_arr1, el_net, el_input, el_output, el_sell, ef);
//                    if(res == result_double){
//                        //更新碳核算记录表
//                        accountingRecord.setState(0);
//                        accountingRecord.setConductor_id(account_id);
//                        UpdateWrapper<AccountingRecord> updateWrapper = new UpdateWrapper<>();
//                        updateWrapper.eq("id",id);
//                        accountingRecordMapper.update(accountingRecord,updateWrapper);
//                        // 获取当前时间的上个月份
//                        YearMonth currentYearMonth = YearMonth.now();
//                        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
//                        // 格式化为"yyyy-MM"的字符串
//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
//                        String previousMonthString = previousYearMonth.format(formatter);
//                        //进行碳核算后cmessage表的更新
//                        CMessage cMessage = new CMessage(enterprise_id,t_limit-res,previousMonthString,t_limit);
//                        cMessageMapper.insert(cMessage);
//
//                        return CommonResponse.createForSuccess("审核通过");
//                    }else{
//                        accountingRecord.setState(2);
//                        accountingRecord.setConductor_id(account_id);
//                        UpdateWrapper<AccountingRecord> updateWrapper = new UpdateWrapper<>();
//                        updateWrapper.eq("id",id);
//                        accountingRecordMapper.update(accountingRecord,updateWrapper);
//                        return CommonResponse.createForError("审核失败");
//                    }
//                    break;
//                case 3 :
//                case 4 :
//                case 5 :
//                case 6 :
//                case 7 :
//                case 8 :
//                case 9 :break;
//                case 10 :
//
//
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
        for (AccountingRecord accountingRecord : accountingRecordList) {
            Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", enterprise_id));
            String account_name = account.getAccount_name();
            Integer enterprise_type = account.getEnterprise_type();
            String s = String.valueOf(accountingRecord.getState());
            if(s.equals("0")){
                s = "通过";
            }else if(s.equals("2")){
                s = "拒绝";
            }else s = "待审核";
            AccountingRecordDto accountingRecordDto = new AccountingRecordDto(accountingRecord.getId(),accountingRecord.getEnterprise_id(),accountingRecord.getMonth(),accountingRecord.getTime(),s,accountingRecord.getVariable_json(),accountingRecord.getResult(),accountingRecord.getConductor_id(),account_name,null);
            EnterpriseType.setDto(accountingRecordDto, enterprise_type);
            accountingRecordDTOList.add(accountingRecordDto);
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

    public CommonResponse<List<AccountingRecordDto>> DataAuditorsGetMyCarbonAccounting(int account_id){
        Account account1 = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", account_id));
        if(account1 !=null){
            List<AccountingRecord> accountingRecordList = accountingRecordMapper.selectList(new QueryWrapper<AccountingRecord>().eq("conductor_id", account_id));
            List<AccountingRecordDto> accountingRecordDTOList = new ArrayList<>();
            for (AccountingRecord accountingRecord : accountingRecordList) {
                int enterprise_id = accountingRecord.getEnterprise_id();
                Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", enterprise_id));
                String account_name = account.getAccount_name();
                Integer enterprise_type = account.getEnterprise_type();
                String s = String.valueOf(accountingRecord.getState());
                if(s.equals("0")){
                    s = "通过";
                }else s = "拒绝";
                AccountingRecordDto accountingRecordDto = new AccountingRecordDto(accountingRecord.getId(),accountingRecord.getEnterprise_id(),accountingRecord.getMonth(),accountingRecord.getTime(),s,accountingRecord.getVariable_json(),accountingRecord.getResult(),accountingRecord.getConductor_id(),account_name,null);
                EnterpriseType.setDto(accountingRecordDto, enterprise_type);
                accountingRecordDTOList.add(accountingRecordDto);
            }
            return CommonResponse.createForSuccess(accountingRecordDTOList);
        }else return CommonResponse.createForError("该管理员不存在");
    }

    public static double[] jsonArrayToDoubleArray(JSONArray jsonArray) {
        double[] doubleArray = new double[jsonArray.size()]; // 创建一个double数组

        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                doubleArray[i] = jsonArray.getDouble(i); // 将JSONArray中的元素转换为double并存储在数组中
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return doubleArray; // 返回转换后的double数组
    }

    public CommonResponse<String> CarbonAccountingRequests(int id,boolean approve,int conductor_id){
        AccountingRecord accountingRecord = accountingRecordMapper.selectOne(new QueryWrapper<AccountingRecord>().eq("id", id));
        if(accountingRecord.getState() !=1){
            return CommonResponse.createForError("该请求已被处理");
        }else{
            if(approve){
                accountingRecord.setState(0);
            }else accountingRecord.setState(2);
            accountingRecord.setConductor_id(conductor_id);
            return CommonResponse.createForSuccess("审核碳请求成功");
        }
    }


}
