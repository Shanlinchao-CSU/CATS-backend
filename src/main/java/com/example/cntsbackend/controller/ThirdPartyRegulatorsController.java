package com.example.cntsbackend.controller;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Transaction;
import com.example.cntsbackend.dto.TransactionDto;
import com.example.cntsbackend.service.AccountService;
import com.example.cntsbackend.service.RegisterApplicationService;
import com.example.cntsbackend.service.TransactionService;
import com.example.cntsbackend.util.MultipartFileToFileConverter;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * ThirdPartyRegulators 第三方监管机构controller
 */

@RestController
public class ThirdPartyRegulatorsController {

    @Autowired
    private RegisterApplicationService registerApplicationService;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService  accountService;

    /**
     * 第三方监管机构查看交易信息
     *
     * @return 交易信息 List<Transaction>
     */
    @GetMapping("/thirdParty/transaction")
    public CommonResponse<List<TransactionDto>> getAllTransactionDatas() {
        return transactionService.getAllTransactionDatas();
    }

    /**
     * 第三方监管机构注册
     *
     * @param file     文件
     * @param name     名字
     * @param password 密码
     * @param phone    电话
     * @param type     类型
     * @param code     验证码
     * @return 注册结果 CommonResponse<String>
     */
    @PostMapping("/thirdParty/info")
    public CommonResponse<String> register(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("phone") String phone,
            @RequestParam("type") int type,
            @RequestParam("code") String code) throws Exception {

        File f = MultipartFileToFileConverter.convert(file);
        if (f == null) {
            return CommonResponse.createForError(2,"传输错误，请重新上传后重试");
        }
        if (accountService.VerifyPhoneCode(phone, code).getData())
            return registerApplicationService.ThirdPartyRegulatorsRegister(f, name, password, phone, type);
        else
            return CommonResponse.createForError(1,"验证码错误");
    }

}
