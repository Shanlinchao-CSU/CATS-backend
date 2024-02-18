package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.persistence.AccountMapper;
import com.example.cntsbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMapper accountMapper;
    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int CODE_LENGTH = 4;

    public int checkPhoneNumberExist(String phoneNumber) {
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phoneNumber));
        if (account != null) {
            return 1; // 号码已存在，发送验证码
        }
        return 0; // 号码不存在，发送错误信息(号码未被注册)
    }

    public String sendVerificationCode(String phoneNumber) {
        if(checkPhoneNumberExist(phoneNumber)==1){
            // 生成四位包括数字、小写字母和大写字母的随机验证码
            String verificationCode = generateVerificationCode();
            return verificationCode; // 发送成功
        }else{
            return null;
        }

    }
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
}
