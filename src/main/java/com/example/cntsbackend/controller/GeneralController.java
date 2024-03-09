package com.example.cntsbackend.controller;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.Signature;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.cntsbackend.service.AccountService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
     * 验证新手机号并发送验证码(修改手机必须先验证，验证通过并且验证码输入正确后再调用下面的changePhone())
     *
     * @param phone 手机
     * @return 验证结果 CommonResponse<String>
     */
    @GetMapping("/general/verify/phone")
    public CommonResponse<String> verifyNewPhone(@PathParam("phone") String phone) {
        return accountService.VerifyNewPhone(phone);
    }

    /**
     * 验证新邮箱并发送验证码(修改邮箱必须先验证，验证通过并且验证码输入正确后调用下面的changeEmail())
     *
     * @param email 邮箱
     * @return 验证结果 CommonResponse<String>
     */
    @GetMapping("/general/verify/email")
    public CommonResponse<String> verifyNewEmail(@PathParam("email") String email) {
        return accountService.VerifyNewEmail(email);
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

    /**
     * 验证数字签名
     *
     * @param signature 数字签名体
     * @return 验证结果 CommonResponse<Boolean>
     */
    @PostMapping("/general/signature")
    public CommonResponse<Boolean> verifyDigitalSignature(@RequestBody Signature signature) {
        return accountService.verifyDigitalSignature(signature.getSignature(), signature.getMessage(), signature.getAddress());
    }

    /**
     * 修改密码
     *
     * @param phone    手机
     * @param password 密码
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/general/password")
    public CommonResponse<String> changePassword(@PathParam("phone") String phone, @PathParam("password") String password) {
        return accountService.changePassword(phone, password);
    }

    /**
     * 修改手机号
     *
     * @param email 邮箱
     * @param phone 手机
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/general/phone")
    public CommonResponse<String> changePhone(@PathParam("email") String email, @PathParam("phone") String phone) {
        return accountService.changePhone(email, phone);
    }

    /**
     * 修改邮箱
     *
     * @param phone 手机
     * @param email 邮箱
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/general/email")
    public CommonResponse<String> changeEmail(@PathParam("phone") String phone, @PathParam("email") String email) {
        return accountService.changeEmail(phone, email);
    }

    /**
     * 用户修改个人信息(需要管理员审核)
     *
     * @param account 账号
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/general/account_info")
    public CommonResponse<String> updateAccountInfo(@RequestBody Account account) {
        return accountService.updateAccountInfo(account);
    }

    /**
     * 找回密码(使用手机号或邮箱)
     *
     * @param str   手机号 或 邮箱
     * @param password 密码
     * @return 找回结果 CommonResponse<String>
     */
    @PatchMapping("/general/str/password")
    public CommonResponse<String> findPassword(@PathParam("str") String str, @PathParam("password") String password) {
        return accountService.findPassword(str, password);
    }


}
