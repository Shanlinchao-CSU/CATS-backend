package com.example.cntsbackend.controller;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.cntsbackend.service.AccountService;

import java.util.Map;

/**
 * General 通用controller
 * 减少重复代码
 */
@Controller
public class GeneralController {

    @Autowired
    private AccountService accountService;

    /**
     * 发送手机验证码
     *
     * @param phone 手机号
     * @return 验证码 CommonResponse<String>
     */
    @GetMapping("/general/code/phone")
    public CommonResponse<String> sendPhoneCode(@PathParam("phone") String phone) {
        return accountService.sendVerificationCodeByPhone(phone);
    }

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return 验证码 CommonResponse<String>
     */
    @GetMapping("/general/code/email")
    public CommonResponse<String> setEmailCode(@PathParam("email") String email) {
        return accountService.sendVerificationCodeByEmail(email);
    }

    /**
     * 账号+密码 登录
     *
     * @param id       账号
     * @param password 密码
     * @return 登录结果 CommonResponse<Map>
     */
    @GetMapping("/general/id")
    public CommonResponse<Map> loginById(@PathParam("id") String id, @PathParam("password") String password) {
        return accountService.loginById(id, password);
    }

    /**
     * 邮箱 登录
     *
     * @param email 邮箱
     * @return 登录结果 CommonResponse<Map>
     */
    @GetMapping("/general/email")
    public CommonResponse<Map> loginByEmail(@PathParam("email") String email) {
        return accountService.loginByEmail(email);
    }

    /**
     * 手机 登录
     *
     * @param phone 手机
     * @return 登录结果 CommonResponse<Map>
     */
    @GetMapping("/general/phone")
    public CommonResponse<Map> loginByPhone(@PathParam("phone") String phone) {
        return accountService.loginByPhone(phone);
    }
}
