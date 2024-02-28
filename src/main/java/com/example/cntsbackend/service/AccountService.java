package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;

import java.util.Map;

public interface AccountService {
    public CommonResponse<String> sendVerificationCodeByPhone(String phoneNumber);
    public CommonResponse<String> sendVerificationCodeByEmail(String email);
    public CommonResponse<Map> loginByPhone(String phone);
    public CommonResponse<Map> loginByEmail(String email);
}
