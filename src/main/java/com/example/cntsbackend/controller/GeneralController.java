package com.example.cntsbackend.controller;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.Signature;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.cntsbackend.service.AccountService;
import org.web3j.abi.datatypes.Bool;

import java.util.Map;

/**
 * General 通用controller
 * 减少重复代码
 */
@RestController
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
    public CommonResponse<String> sendPhoneCode(
            @PathParam("phone") String phone) {
        return accountService.sendVerificationCodeByPhone(phone);
    }

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return 验证码 CommonResponse<String>
     */
    @GetMapping("/general/code/email")
    public CommonResponse<String> setEmailCode(
            @PathParam("email") String email) {
        return accountService.sendVerificationCodeByEmail(email);
    }

    /**
     * 验证新手机号并发送验证码(修改手机必须先验证，验证通过并且验证码输入正确后再调用下面的changePhone())
     *
     * @param phone 手机
     * @return 验证结果 CommonResponse<String>
     */
    @GetMapping("/general/verify/phone")
    public CommonResponse<String> verifyNewPhone(
            @PathParam("phone") String phone) {
        return accountService.VerifyNewPhone(phone);
    }

    /**
     * 验证新邮箱并发送验证码(修改邮箱必须先验证，验证通过并且验证码输入正确后调用下面的changeEmail())
     *
     * @param email 邮箱
     * @return 验证结果 CommonResponse<String>
     */
    @GetMapping("/general/verify/email")
    public CommonResponse<String> verifyNewEmail(
            @PathParam("email") String email) {
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
    public CommonResponse<Map> loginById(
            @PathParam("id") String id,
            @PathParam("password") String password) {
        return accountService.loginById(id, password);
    }

    /**
     * 邮箱 登录
     *
     * @param email 邮箱
     * @param code 验证码
     * @return 登录结果 CommonResponse<Map>
     */
    @GetMapping("/general/email")
    public CommonResponse<Map> loginByEmail(
            @PathParam("email") String email,
            @PathParam("code")String code) {
        if (accountService.VerifyEmailCode(email, code).getData())
            return accountService.loginByEmail(email);
        else
            return CommonResponse.createForError("验证码错误");
    }

    /**
     * 验证手机验证码是否正确
     *
     * @param phoneNumber 手机号
     * @param code 验证码
     * @return 验证结果 CommonResponse<String>
     */
    @GetMapping("/general/verify/phone/code")
    public CommonResponse<Boolean> verifyPhoneCode(
            @PathParam("phoneNumber") String phoneNumber,
            @PathParam("code") String code) {
        return accountService.VerifyPhoneCode(phoneNumber, code);
    }

    /**
     * 验证邮箱验证码是否正确
     *
     * @param email 邮箱
     * @param code 验证码
     * @return 验证结果 CommonResponse<String>
     */
    @GetMapping("/general/verify/email/code")
    public CommonResponse<Boolean> verifyEmailCode(
            @PathParam("email") String email,
            @PathParam("code") String code) {
        return accountService.VerifyEmailCode(email, code);
    }

    /**
     * 手机 登录
     *
     * @param phone 手机
     * @param code 验证码
     * @return 登录结果 CommonResponse<Map>
     */
    @GetMapping("/general/phone")
    public CommonResponse<Map> loginByPhone(
            @PathParam("phone") String phone,
            @PathParam("code") String code) {
        if (accountService.VerifyPhoneCode(phone, code).getData())
            return accountService.loginByPhone(phone);
        else
            return CommonResponse.createForError("验证码错误");
    }

    /**
     * 后端获取区块链相关信息(公钥密钥,如果存在,直接return,不存在则会添加到数据库中)
     *
     * @param account_id 账号ID
     * @param public_key 公钥
     * @param secret_key 密钥
     * @return 获取结果 CommonResponse<String>
     */
    @GetMapping("/general/block/key")
    public CommonResponse<String> getInfo(
            @PathParam("account_id") int account_id,
            @PathParam("public_key") String public_key,
            @PathParam("secret_key") String secret_key) {
        return accountService.getInfo(account_id, public_key, secret_key);
    }

    /**
     * 后端获取区块链相关信息(碳币、碳额度、剩余额度,每次登录都要进行数据更新)
     *
     * @param account_id 账号ID
     * @param t_coin 碳币
     * @param t_remain 剩余额度
     * @param t_limit 碳额度
     * @return 获取结果 CommonResponse<String>
     */
    @GetMapping("/general/block/info/update")
    public CommonResponse<String> getT_coinAndT_limit(
            @PathParam("account_id") int account_id,
            @PathParam("t_coin") double t_coin,
            @PathParam("t_remain") double t_remain,
            @PathParam("t_limit") double t_limit) {
        return accountService.getT_coinAndT_limit(account_id, t_coin, t_remain, t_limit);
    }

    /**
     * 验证数字签名
     *
     * @param signature 数字签名体
     * @return 验证结果 CommonResponse<Boolean>
     */
    @PostMapping("/general/signature")
    public CommonResponse<Boolean> verifyDigitalSignature(
            @RequestBody Signature signature) {
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
    public CommonResponse<String> changePassword(
            @PathParam("phone") String phone,
            @PathParam("password") String password) {
        return accountService.changePassword(phone, password);
    }

    /**
     * 修改手机号
     *
     * @param email 邮箱
     * @param phone 手机
     * @param code 验证码
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/general/phone")
    public CommonResponse<String> changePhone(
            @PathParam("email") String email,
            @PathParam("phone") String phone,
            @PathParam("code") String code) {
        if (accountService.VerifyPhoneCode(phone, code).getData())
            return accountService.changePhone(email, phone);
        else
            return CommonResponse.createForError("验证码错误");
    }

    /**
     * 修改邮箱
     *
     * @param phone 手机
     * @param email 邮箱
     * @param code 验证码
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/general/email")
    public CommonResponse<String> changeEmail(
            @PathParam("phone") String phone,
            @PathParam("email") String email,
            @PathParam("code") String code) {
        if (accountService.VerifyEmailCode(email, code).getData())
            return accountService.changeEmail(phone, email);
        else
            return CommonResponse.createForError("验证码错误");
    }

    /**
     * 用户修改个人信息(需要管理员审核)
     *
     * @param account 账号
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/general/account_info")
    public CommonResponse<String> updateAccountInfo(
            @RequestBody Account account) {
        return accountService.updateAccountInfo(account);
    }

    /**
     * 找回密码(使用手机号或邮箱)
     *
     * @param str   手机号 或 邮箱
     * @param password 密码
     * @param code 验证码
     * @return 找回结果 CommonResponse<String>
     */
    @PatchMapping("/general/str/password")
    public CommonResponse<String> findPassword(
            @PathParam("str") String str,
            @PathParam("password") String password,
            @PathParam("code") String code) {
        if (accountService.VerifyPhoneCode(str, code).getData() || accountService.VerifyEmailCode(str, code).getData())
            return accountService.findPassword(str, password);
        else
            return CommonResponse.createForError("验证码错误");
    }


}
