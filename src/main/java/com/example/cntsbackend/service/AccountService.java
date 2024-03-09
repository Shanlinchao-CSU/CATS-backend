package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.RegisterApplication;

import java.util.List;
import java.util.Map;

public interface AccountService {

    //----------------------------------企业用户--------------------------------

    //发送手机验证码
    public CommonResponse<String> sendVerificationCodeByPhone(String phoneNumber);
    //发送邮箱验证码
    public CommonResponse<String> sendVerificationCodeByEmail(String email);
    //手机登录
    public CommonResponse<Map> loginByPhone(String phone);
    //邮箱登录
    public CommonResponse<Map> loginByEmail(String email);
    //TODO:(修改)ID或邮箱或手机+密码登录
    CommonResponse<Map> loginById(String str,String password);
    //修改密码
    CommonResponse<String> changePassword(String phone,String password);
    //验证新手机号并发送验证码(修改手机必须先验证，验证通过并且验证码输入正确后再调用下面的changePhone())
    CommonResponse<String> VerifyNewPhone(String phone);
    //验证新邮箱并发送验证码(修改邮箱必须先验证，验证通过并且验证码输入正确后调用下面的changeEmail())
    CommonResponse<String> VerifyNewEmail(String email);
    //修改手机号
    CommonResponse<String> changePhone(String email,String phone);
    //修改邮箱
    CommonResponse<String> changeEmail(String phone,String email);
    //用户修改个人信息(需要管理员审核)
    CommonResponse<String> updateAccountInfo(Account account);
    //找回密码(使用手机号或邮箱号)
    CommonResponse<String> findPassword(String str,String password);
    //验证数字签名
    CommonResponse<Boolean> verifyDigitalSignature(String signature, String message, String address);

    //-----------------------------------管理员--------------------------------------

    //TODO:（改动，需要添加管理人员的id作为参数以及当前日期年-月）同意注册
    CommonResponse<String> AgreeApplication(String phone ,String email, int account_id,String month);
    //TODO:（改动，需要添加一个管理人员的id作为参数）拒绝注册
    CommonResponse<String> RefuseApplication(String phone ,String email, int account_id);
    //同意修改个人信息
    CommonResponse<String> AgreeUpdateAccountInfo(String phone ,String email);
    //拒绝修改个人信息
    CommonResponse<String> RefuseUpdateAccountInfo(String phone ,String email);
    //TODO:（新加）管理员获取所有还未被审核的注册申请
    CommonResponse<List<RegisterApplication>> getPendingReviewAccount();
}
