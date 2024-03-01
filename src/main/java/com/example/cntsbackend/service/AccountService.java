package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;

import java.util.Map;

public interface AccountService {
    //发送手机验证码
    public CommonResponse<String> sendVerificationCodeByPhone(String phoneNumber);
    //发送邮箱验证码
    public CommonResponse<String> sendVerificationCodeByEmail(String email);
    //手机登录
    public CommonResponse<Map> loginByPhone(String phone);
    //邮箱登录
    public CommonResponse<Map> loginByEmail(String email);
    //ID+密码登录
    CommonResponse<Map> loginById(String id,String password);
}
