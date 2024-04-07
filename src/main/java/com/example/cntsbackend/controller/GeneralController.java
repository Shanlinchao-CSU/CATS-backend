package com.example.cntsbackend.controller;

import com.example.cntsbackend.annotation.LOG;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.Signature;
import com.example.cntsbackend.dto.BlockInfoDto;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.cntsbackend.service.AccountService;
import org.web3j.abi.datatypes.Bool;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * General 通用controller
 * 减少重复代码
 */
@RestController
public class GeneralController {

    @Autowired
    private AccountService accountService;

    private static final String MODULE_NAME = "通用模块";
    private static final String MODULE_VERSION = "v1.0.0";

    /**
     * 发送手机验证码
     *
     * @param phone 手机号
     * @return 验证码 CommonResponse<String>
     */
    @GetMapping("/general/code/phone")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> sendPhoneCode(
            @PathParam("phone") String phone) throws Exception {
        return accountService.sendVerificationCodeByPhone(phone);
    }

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return 验证码 CommonResponse<String>
     */
    @GetMapping("/general/code/email")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> setEmailCode(
            @PathParam("email") String email) throws Exception {
        return accountService.sendVerificationCodeByEmail(email);
    }

    /**
     * 验证新手机号并发送验证码(修改手机必须先验证，验证通过并且验证码输入正确后再调用下面的changePhone())
     *
     * @param phone 手机
     * @return 验证结果 CommonResponse<String>
     */
    @GetMapping("/general/verify/phone")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> verifyNewPhone(
            @PathParam("phone") String phone) throws Exception {
        return accountService.VerifyNewPhone(phone);
    }

    /**
     * 验证新邮箱并发送验证码(修改邮箱必须先验证，验证通过并且验证码输入正确后调用下面的changeEmail())
     *
     * @param email 邮箱
     * @return 验证结果 CommonResponse<String>
     */
    @GetMapping("/general/verify/email")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> verifyNewEmail(
            @PathParam("email") String email) throws Exception {
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
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<Map> loginById(
            @PathParam("id") String id,
            @PathParam("password") String password) throws Exception {
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
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<Map> loginByEmail(
            @PathParam("email") String email,
            @PathParam("code")String code) throws Exception {
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
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
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
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
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
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<Map> loginByPhone(
            @PathParam("phone") String phone,
            @PathParam("code") String code) throws Exception {
        if (accountService.VerifyPhoneCode(phone, code).getData())
            return accountService.loginByPhone(phone);
        else
            return CommonResponse.createForError("验证码错误");
    }

    /**
     * 后端获取区块链相关信息(碳币、碳额度、剩余额度,每次登录都要进行数据更新)
     *
     * @param blockInfoDto 区块链信息
     * @return 获取结果 CommonResponse<String>
     */
    @GetMapping("/general/block/info/update")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> getT_coinAndT_limit(
            @RequestBody List<BlockInfoDto> blockInfoDto) {
        return accountService.getT_coinAndT_limit(blockInfoDto);
    }

    /**
     * 后端获取区块链相关信息 ———— t_limit(碳额度)
     *
     * @param account_id 账号ID
     * @param t_limit 碳额度
     * @return 获取结果 CommonResponse<String>
     */
    @GetMapping("/general/block/info/t_limit")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> getT_limit(
            @PathParam("account_id") int account_id,
            @PathParam("t_limit") double t_limit) {
        return accountService.getT_limit(account_id, t_limit);
    }

    /**
     * 验证数字签名
     *
     * @param signature 数字签名体
     * @return 验证结果 CommonResponse<Boolean>
     */
    @PostMapping("/general/signature")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
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
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> changePassword(
            @PathParam("phone") String phone,
            @PathParam("password") String password) throws Exception {
        return accountService.changePassword(phone, password);
    }

    /**
     * 修改手机号
     *
     * @param phone 手机
     * @param account_id 账号ID
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/general/phone")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> changePhone(
            @PathParam("phone") String phone,
            @PathParam("account_id") int account_id) throws Exception {
        return accountService.changePhone(phone, account_id);
    }

    /**
     * 修改邮箱
     *
     * @param email 邮箱
     * @param account_id 账号ID
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/general/email")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> changeEmail(
            @PathParam("email") String email,
            @PathParam("account_id") int account_id) throws Exception {
        return accountService.changeEmail(email, account_id);
    }

    /**
     * 用户修改个人信息(需要管理员审核)
     *
     * @param account 账号
     * @return 修改结果 CommonResponse<String>
     */
    @PatchMapping("/general/account_info")
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
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
    @LOG(moduleName = MODULE_NAME, moduleVersion = MODULE_VERSION)
    public CommonResponse<String> findPassword(
            @PathParam("str") String str,
            @PathParam("password") String password,
            @PathParam("code") String code) throws Exception {
        if (accountService.VerifyPhoneCode(str, code).getData() || accountService.VerifyEmailCode(str, code).getData())
            return accountService.findPassword(str, password);
        else
            return CommonResponse.createForError("验证码错误");
    }


}
