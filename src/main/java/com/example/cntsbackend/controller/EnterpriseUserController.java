package com.example.cntsbackend.controller;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.QuotaSale;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.dto.QuotaSaleDto;
import com.example.cntsbackend.dto.TransactionDto;
import com.example.cntsbackend.service.QuotaSaleService;
import com.example.cntsbackend.service.RegisterApplicationService;
import com.example.cntsbackend.service.TransactionService;
import com.example.cntsbackend.util.MultipartFileToFileConverter;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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


    /**
     * 企业获取自己已完成的交易
     *
     * @param account_id 账号ID
     * @return 交易信息 List<Transaction>
     */
    @GetMapping("/enterprise/transaction/finished/{account_id}")
    public CommonResponse<List<TransactionDto>> getMyFinishedTransactionDatas(@PathVariable("account_id") int account_id) {
        return transactionService.getMyFinishedTransactionDatas(account_id);
    }

    /**
     * 企业查看上月额度剩余（未卖出）
     *
     * @param account_id 账号ID
     * @return 剩余额度 CommonResponse<QuotaSale>
     */
    @GetMapping("/enterprise/transaction/remain/{account_id}")
    public CommonResponse<QuotaSale> getRemain(@PathVariable("account_id") int account_id) {
        return quotaSaleService.getRemain(account_id);
    }

    /**
     * 获取所有企业上月额度剩余（用于购买展示）
     *
     * @return 所有企业上月额度剩余 CommonResponse<List<QuotaSaleDto>>
     */
    @GetMapping("/enterprise/transaction/remain")
    public CommonResponse<List<QuotaSaleDto>> getAllRemain() {
        return quotaSaleService.getAllRemain();
    }

    /**
     * 企业修改单价(id为发布信息的id)
     *
     * @param id          交易信息ID
     * @param unit_price        单价
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/enterprise/transaction/price")
    public CommonResponse<String> ModifyUnitPrice(@PathParam("id") int id, @PathParam("unit_price") double unit_price) {
        return quotaSaleService.ModifyUnitPrice(id, unit_price);
    }

    /**
     * 额度购买，第一个参数为买家id，第二个参数为额度发布信息的id，第三个参数为要买的额度
     *
     * @param account_id    买家ID
     * @param quotaSale_id  额度发布信息ID
     * @param amount        要买的额度
     * @return 购买结果 CommonResponse<String>
     */
    @PatchMapping("/enterprise/transaction/amount")
    public CommonResponse<String> CompleteTransaction(@PathParam("account_id") int account_id, @PathParam("quotaSale_id") int quotaSale_id, @PathParam("amount") double amount) {
        return transactionService.CompleteTransaction(account_id, quotaSale_id, amount);
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
     * @return 注册结果 CommonResponse<String>
     */
    @PostMapping("/enterprise/info")
    public CommonResponse<String> register(@RequestParam("file") MultipartFile file, @RequestParam("name") String name, @RequestParam("password") String password, @RequestParam("phone") String phone, @RequestParam("enterprise_type") int enterprise_type, @RequestParam("type") int type) throws IOException {
        File f = MultipartFileToFileConverter.convert(file);
        if (f == null) {
            return CommonResponse.createForSuccess("文件已存在，请重新上传后重试");
        }else {
            String etag = MultipartFileToFileConverter.etag(f);
            Boolean result = MultipartFileToFileConverter.etag(f, etag);
            if (!result) {
                return CommonResponse.createForSuccess("传输错误，请重新上传后重试");
            }else
                return registerApplicationService.EnterpriseUserRegister(f, name, password, phone, enterprise_type, type);
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
    public CommonResponse<String> publishTransaction(@PathParam("account_id") int account_id, @PathParam("quota") double quota, @PathParam("unit_price") double unit_price) {
        return quotaSaleService.PublishTransaction(account_id, quota, unit_price);
    }

    /**
     * 企业取消已发布的交易信息
     *
     * @param id 交易信息ID
     * @return 取消结果 CommonResponse<String>
     */
    @DeleteMapping("/enterprise/transaction/{id}")
    public CommonResponse<String> cancelTransactionData(@PathVariable("id") int id) {
        return quotaSaleService.cancelTransactionData(id);
    }

}
