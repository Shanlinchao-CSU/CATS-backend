package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.persistence.AccountMapper;
import com.example.cntsbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMapper accountMapper;
    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int CODE_LENGTH = 4;

    //检验号码是否已经注册
    public int checkPhoneNumberExist(String phoneNumber) {
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phoneNumber));
        if (account != null) {
            return 0; // 号码已存在，发送验证码
        }
        return 1; // 号码不存在，发送错误信息(号码未被注册)
    }
    //检验邮箱是否已经注册
    public int checkEmailExist(String email) {
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("email", email));
        if (account != null) {
            return 0; // 号码已存在，发送验证码
        }
        return 1; // 号码不存在，发送错误信息(号码未被注册)
    }

    //发送手机验证码
    public CommonResponse<Account> sendVerificationCodeByPhone(String phoneNumber) {
        if(checkPhoneNumberExist(phoneNumber)==0){
            // 生成四位包括数字、小写字母和大写字母的随机验证码
            String verificationCode = generateVerificationCode();
            return CommonResponse.createForSuccess(verificationCode); // 发送成功
        }else{
            return CommonResponse.createForError("0");
        }
    }

    //发送邮箱验证码
    public CommonResponse<Account> sendVerificationCodeByEmail(String email) {
        if(checkEmailExist(email)==0){
            // 生成四位包括数字、小写字母和大写字母的随机验证码
            String verificationCode = generateVerificationCode();
            return CommonResponse.createForSuccess(verificationCode); // 发送成功
        }else{
            return CommonResponse.createForError("0");
        }
    }
    //生成四位验证码
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char character = CHARACTERS.charAt(index);
            codeBuilder.append(character);
        }

        return codeBuilder.toString();
    }

    //邮箱登录
    public CommonResponse<Account> loginByEmail(String email){
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("email", email));
        return CommonResponse.createForSuccess(account);
    }
    //手机号码登录
    public CommonResponse<Account> loginByPhone(String phone){
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
        return CommonResponse.createForSuccess(account);
    }
}
