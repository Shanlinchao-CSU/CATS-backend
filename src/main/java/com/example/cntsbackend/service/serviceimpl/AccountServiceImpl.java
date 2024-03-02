package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.CMessage;
import com.example.cntsbackend.domain.RegisterApplication;
import com.example.cntsbackend.persistence.AccountMapper;
import com.example.cntsbackend.persistence.CMessageMapper;
import com.example.cntsbackend.persistence.RegisterApplicationMapper;
import com.example.cntsbackend.service.AccountService;
import com.example.cntsbackend.util.SendMailUtil;
import com.example.cntsbackend.util.SendPhoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RegisterApplicationMapper registerApplicationMapper;
    @Autowired
    private CMessageMapper cMessageMapper;
    private static final String CHARACTERS = "0123456789";
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
            return 0; // 邮箱已存在，发送验证码
        }
        return 1; // 邮箱不存在，发送错误信息(邮箱未被注册)
    }

    //发送手机验证码
    public CommonResponse<String> sendVerificationCodeByPhone(String phoneNumber) {
        if(checkPhoneNumberExist(phoneNumber)==0){
            // 生成四位包括数字、小写字母和大写字母的随机验证码
            String verificationCode = generateVerificationCode();
            SendPhoneUtil.sendSMS(phoneNumber,verificationCode);
            return CommonResponse.createForSuccess("SUCCESS",verificationCode); // 发送成功
        }else{
            return CommonResponse.createForError("0");
        }
    }

    //发送邮箱验证码
    public CommonResponse<String> sendVerificationCodeByEmail(String email) {
        if(checkEmailExist(email)==0){
            // 生成四位包括数字、小写字母和大写字母的随机验证码
            String verificationCode = generateVerificationCode();
            SendMailUtil.sendQQEmail(email, Integer.parseInt(verificationCode));
            return CommonResponse.createForSuccess("SUCCESS",verificationCode); // 发送成功
        }else{
            return CommonResponse.createForError("ERROR","0");
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
    public CommonResponse<Map> loginByEmail(String email){
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("email", email));
        Map<String, Object> map = new HashMap<>();
        String token = UUID.randomUUID().toString();

        map.put("Account",account);
        map.put("token",token);

        return CommonResponse.createForSuccess("邮箱登录",map);
    }
    //手机号码登录
    public CommonResponse<Map> loginByPhone(String phone){
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
        Map<String, Object> map = new HashMap<>();
        String token = UUID.randomUUID().toString();

        map.put("Account",account);
        map.put("token",token);

        return CommonResponse.createForSuccess("手机号码登录",map);
    }
    public CommonResponse<Map> loginById(String id,String password){
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_name", id).eq("password", password));
        Map<String, Object> map = new HashMap<>();
        String token = UUID.randomUUID().toString();

        map.put("Account",account);
        map.put("token",token);

        return CommonResponse.createForSuccess("id+密码登录",map);
    }
    public CommonResponse<String> AgreeApplication(String name ,String password ,String phone ,String email ,int enterprise_type ,int type){
        //TODO:确定cmessage表碳额度的初始值
        accountMapper.insert(new Account(name, password, phone, email, enterprise_type, type));
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone).eq("email", email));
        int account_id = account.getAccount_id();
        cMessageMapper.insert(new CMessage(account_id,500));
        registerApplicationMapper.delete(new QueryWrapper<RegisterApplication>().eq("phone",phone).eq("email",email));
        return CommonResponse.createForSuccess("审核成功，同意注册");
    }

    public CommonResponse<String> RefuseApplication(String name ,String password ,String phone ,String email ,int enterprise_type ,int type){
        registerApplicationMapper.delete(new QueryWrapper<RegisterApplication>().eq("phone",phone).eq("email",email));
        return CommonResponse.createForSuccess("审核成功，拒绝注册");
    }
}
