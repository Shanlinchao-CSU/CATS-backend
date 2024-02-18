package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;

public interface AccountService {
    public CommonResponse<Account> sendVerificationCodeByPhone(String phoneNumber);
    public CommonResponse<Account> sendVerificationCodeByEmail(String email);
    public CommonResponse<Account> loginByPhone(String phone);
    public CommonResponse<Account> loginByEmail(String email);
}
