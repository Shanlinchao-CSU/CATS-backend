package com.example.cntsbackend.controller;

import com.example.cntsbackend.annotation.LOG;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.AccountingRecord;
import com.example.cntsbackend.domain.QuotaSale;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.dto.AccountDto;
import com.example.cntsbackend.dto.AccountingRecordDto;
import com.example.cntsbackend.dto.QuotaSaleDto;
import com.example.cntsbackend.dto.TransactionDto;
import com.example.cntsbackend.service.*;
import com.example.cntsbackend.util.MultipartFileToFileConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * EnterpriseUser 企业用户controller
 */
@RestController
public class EnterpriseUserController {

    @Autowired
    private RegisterApplicationService registerApplicationService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private QuotaSaleService quotaSaleService;
    @Autowired
    private AccountingRecordService accountingRecordService;
    @Autowired
    private AccountService accountService;

    private static final String MODULE_NAME = "企业用户模块";
    private static final String MODULE_VERSION = "v1.0.0";


    /**
     * 企业获取自己已完成的交易
     *
     * @param account_id 账号ID
     * @return 交易信息 List<Transaction>
     */
    @GetMapping("/enterprise/transaction/finished/{account_id}")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<List<TransactionDto>> getMyFinishedTransactionDatas(
            @PathVariable("account_id") int account_id) {
        return transactionService.getMyFinishedTransactionDatas(account_id);
    }

    /**
     * 企业查看上月额度剩余（未卖出）
     *
     * @param account_id 账号ID
     * @return 剩余额度 CommonResponse<List<QuotaSale>>
     */
    @GetMapping("/enterprise/transaction/remain/{account_id}")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<List<QuotaSale>> getRemain(
            @PathVariable("account_id") int account_id) {
        return quotaSaleService.getRemain(account_id);
    }

    /**
     * 获取所有企业上月额度剩余（用于购买展示）
     *
     * @return 所有企业上月额度剩余 CommonResponse<List<QuotaSaleDto>>
     */
    @GetMapping("/enterprise/transaction/remain")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<List<QuotaSaleDto>> getAllRemain() throws Exception {
        return quotaSaleService.getAllRemain();
    }

    /**
     * 企业查看以往自己的碳核算记录
     *
     * @param enterprise_id 企业ID
     * @return 碳核算记录 CommonResponse<List<AccountingRecordDto>>
     */
    @GetMapping("/enterprise/accounting_record/{enterprise_id}")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<List<AccountingRecordDto>> getMyCarbonAccounting(
            @PathVariable("enterprise_id") int enterprise_id) {
        return accountingRecordService.getMyCarbonAccounting(enterprise_id);
    }


    /**
     * 企业修改单价(id为发布信息的id)
     *
     * @param id         交易信息ID
     * @param unit_price 单价
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/enterprise/transaction/price")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> ModifyUnitPrice(
            @PathParam("id") int id,
            @PathParam("unit_price") double unit_price) {
        return quotaSaleService.ModifyUnitPrice(id, unit_price);
    }

    /**
     * 额度购买，第一个参数为买家id，第二个参数为额度发布信息的id，第三个参数为要买的额度
     *
     * @param account_id   买家ID
     * @param quotaSale_id 额度发布信息ID
     * @param amount       要买的额度
     * @return 购买结果 CommonResponse<String>
     */
    @PatchMapping("/enterprise/transaction/amount")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> CompleteTransaction(
            @PathParam("account_id") int account_id,
            @PathParam("quotaSale_id") int quotaSale_id,
            @PathParam("amount") double amount) {
        return transactionService.CompleteTransaction(account_id, quotaSale_id, amount);
    }

    /**
     * 用户修改碳核算记录(未被审核)
     * @param id 碳核算记录ID
     * @param variable_json 碳核算数据
     * @param result 碳核算结果
     * @param supporting_material 证明材料
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/enterprise/accounting_record")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> ModifyMyCarbonAccounting(
            @PathParam("id") int id,
            @PathParam("variable_json") String variable_json,
            @PathParam("result") String result,
            @PathParam("supporting_material") String supporting_material) {

        return accountingRecordService.ModifyMyCarbonAccounting(id,new AccountingRecord(variable_json,result,supporting_material));
    }


    /**
     * 根据公钥获取企业信息
     *
     * @param public_key 公钥
     * @return 企业信息 CommonResponse<List<AccountDto>>
     */
    @PostMapping("/enterprise/info/address")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<List<AccountDto>> getEnterpriseInfoByAddress(
            @RequestBody String public_key) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(public_key);

            List<String> publicKeyList = new ArrayList<>();
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    publicKeyList.add(node.asText());
                }
            }
            return accountService.getEnterpriseInfoByAddress(publicKeyList);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.createForError(1,"解析错误");
        }
    }

    /**
     * 企业用户注册
     *
     * @param file            文件
     * @param name            账号
     * @param password        密码
     * @param phone           电话
     * @param enterprise_type 企业类型
     * @param type            类型
     * @param code            验证码
     * @param signature       数字签名
     * @param message         消息
     * @param address         地址
     * @return 注册结果 CommonResponse<String>
     */
    @PostMapping("/enterprise/info")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> register(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("phone") String phone,
            @RequestParam("enterprise_type") int enterprise_type,
            @RequestParam("type") int type,
            @RequestParam("code") String code,
            @RequestParam("signature") String signature,
            @RequestParam("message") String message,
            @RequestParam("address") String address,
            @RequestParam("public_key") String public_key) throws Exception {
        System.out.println("11111111");
        File f = MultipartFileToFileConverter.convert(file);
        if (f == null) {
            return CommonResponse.createForError(2,"文件已存在，请重新上传后重试");
        }
        if (accountService.verifyDigitalSignature(signature, message, address).getData()){
            if (accountService.VerifyPhoneCode(phone, code).getData()) {
                return registerApplicationService.EnterpriseUserRegister(f, name, password, phone, enterprise_type, type, public_key);
            }
            else
                return CommonResponse.createForError(1,"验证码错误");
        }else{
            return CommonResponse.createForError(3,"数字签名错误");
        }
    }

    /**
     * 企业发布交易信息
     *
     * @param account_id 企业账号ID
     * @param quota      额度
     * @param unit_price 单价
     * @return 发布结果 CommonResponse<String>
     */
    @PostMapping("/enterprise/transaction/publish")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> publishTransaction(
            @PathParam("account_id") int account_id,
            @PathParam("quota") double quota,
            @PathParam("unit_price") double unit_price) {
        return quotaSaleService.PublishTransaction(account_id, quota, unit_price);
    }

    /**
     * 企业提交碳核算
     *
     * @param enterprise_id 企业ID
     * @param variable_json 变量JSON
     * @param result        结果
     * @param file          文件
     * @return 提交结果 CommonResponse<String>
     */
    @PostMapping("/enterprise/accounting_record")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> SubmitCarbonAccounting(
            @RequestParam("enterprise_id") int enterprise_id,
            @RequestParam("variable_json") String variable_json,
            @RequestParam("result") String result,
            @RequestParam("file") MultipartFile file) throws IOException {
        File f = MultipartFileToFileConverter.convert(file);
        if (f == null) {
            return CommonResponse.createForSuccess("文件已存在，请重新上传后重试");
        }
        return accountingRecordService.SubmitCarbonAccounting(enterprise_id, variable_json, result, f);
    }

    /**
     * 企业取消已发布的交易信息
     *
     * @param id 交易信息ID
     * @return 取消结果 CommonResponse<String>
     */
    @DeleteMapping("/enterprise/transaction/{id}")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> cancelTransactionData(
            @PathVariable("id") int id) {
        return quotaSaleService.cancelTransactionData(id);
    }

    /**
     * 用户取消碳核算记录(未被审核)
     *
     * @param id 碳核算记录ID
     * @return 取消结果 CommonResponse<String>
     */
    @DeleteMapping("/enterprise/accounting_record/{id}")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> CancelMyCarbonAccounting(
            @PathVariable("id") int id) {
        return accountingRecordService.CancelMyCarbonAccounting(id);
    }

}
