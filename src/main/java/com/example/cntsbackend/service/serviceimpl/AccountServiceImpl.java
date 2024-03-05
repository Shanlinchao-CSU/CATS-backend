package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.CMessage;
import com.example.cntsbackend.domain.RegisterApplication;
import com.example.cntsbackend.domain.UpdateAccount;
import com.example.cntsbackend.persistence.AccountMapper;
import com.example.cntsbackend.persistence.CMessageMapper;
import com.example.cntsbackend.persistence.RegisterApplicationMapper;
import com.example.cntsbackend.persistence.UpdateAccountMapper;
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
    @Autowired
    private UpdateAccountMapper updateAccountMapper;
    private static final String CHARACTERS = "0123456789";
    private static final int CODE_LENGTH = 4;

    //--------------------------------------------企业用户--------------------------------------------------

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
    public CommonResponse<String> sendVerificationCodeByChangePhone(String phoneNumber){
        String verificationCode = generateVerificationCode();
        SendPhoneUtil.sendSMS(phoneNumber,verificationCode);
        return CommonResponse.createForSuccess("SUCCESS",verificationCode); // 发送成功
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
    public CommonResponse<String> sendVerificationCodeByChangeEmail(String email){
        String verificationCode = generateVerificationCode();
        SendMailUtil.sendQQEmail(email, Integer.parseInt(verificationCode));
        return CommonResponse.createForSuccess("SUCCESS",verificationCode); // 发送成功
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
        if(account!=null){
            Map<String, Object> map = new HashMap<>();
            String token = UUID.randomUUID().toString();

            map.put("Account",account);
            map.put("token",token);

            return CommonResponse.createForSuccess("邮箱登录成功",map);
        }else return CommonResponse.createForError("邮箱登录失败");
    }
    //手机号码登录
    public CommonResponse<Map> loginByPhone(String phone){
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
        if(account!=null){
            Map<String, Object> map = new HashMap<>();
            String token = UUID.randomUUID().toString();

            map.put("Account",account);
            map.put("token",token);

            return CommonResponse.createForSuccess("手机号码登录成功",map);
        }else return CommonResponse.createForError("手机号码登录失败");
    }
    public CommonResponse<Map> loginById(String name,String password){
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_name", name).eq("password", password));
        if(account!=null){
            Map<String, Object> map = new HashMap<>();
            String token = UUID.randomUUID().toString();

            map.put("Account",account);
            map.put("token",token);

            return CommonResponse.createForSuccess("id+密码登录成功",map);
        }else return CommonResponse.createForError("id+密码登录失败");
    }

    public CommonResponse<String> changePassword(String phone,String password){
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
        if(account!=null){
            account.setPassword(password);

            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("phone",phone);
            accountMapper.update(account, updateWrapper);

            return CommonResponse.createForSuccess("修改密码成功");
        }else return CommonResponse.createForError("用户不存在，修改密码失败");
    }
    public CommonResponse<String> VerifyNewPhone(String phone){
        if(checkPhoneNumberExist(phone)==0){
            return CommonResponse.createForError("手机号已被使用");
        }else{
            return sendVerificationCodeByChangePhone(phone);
        }
    }
    public CommonResponse<String> changePhone(String phone,String email){
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("email", email));
        if(account!=null){
            account.setPhone(phone);

            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("email",email);
            accountMapper.update(account, updateWrapper);

            return CommonResponse.createForSuccess("修改手机号成功");
        }else return CommonResponse.createForError("用户不存在，修改手机号失败");
    }
    public CommonResponse<String> VerifyNewEmail(String email){
        if(checkEmailExist(email)==0){
            return CommonResponse.createForError("邮箱已被使用");
        }else{
            return sendVerificationCodeByChangeEmail(email);
        }
    }
    public CommonResponse<String> changeEmail(String phone,String email){
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
        if(account!=null){
            account.setEmail(email);

            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("phone",phone);
            accountMapper.update(account, updateWrapper);

            return CommonResponse.createForSuccess("修改邮箱成功");
        }else return CommonResponse.createForError("用户不存在，修改邮箱失败");
    }
    public CommonResponse<String> updateAccountInfo(Account account){
        UpdateAccount updateAccount = new UpdateAccount(account.getAccount_name(),account.getPassword(),account.getPhone(),account.getEmail(),account.getType(),account.getEnterprise_type(),account.getEnterprise_address(),account.getFile());
        updateAccountMapper.insert(updateAccount);
        return CommonResponse.createForSuccess("提交修改信息成功，等待审核");
    }

    public CommonResponse<String> findPassword(String phone,String password){
        return changePassword(phone,password);
    }







    //-------------------------------------------管理员------------------------------------------------------

    public CommonResponse<String> AgreeApplication(String phone ,String email){
        RegisterApplication registerApplication = registerApplicationMapper.selectOne(new QueryWrapper<RegisterApplication>().eq("phone", phone).eq("email", email));
        String account_name = registerApplication.getAccount_name();
        String password = registerApplication.getPassword();
        int enterprise_type = registerApplication.getEnterprise_type();
        String file_address = registerApplication.getFile_address();
        String enterprise_address = registerApplication.getEnterprise_address();
        Integer type = registerApplication.getType();
        accountMapper.insert(new Account(account_name, password, phone, email, type,enterprise_type, enterprise_address ,file_address));
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone).eq("email", email));
        int account_id = account.getAccount_id();
        //TODO:确定cmessage表碳额度的初始值
        cMessageMapper.insert(new CMessage(account_id,500));
        registerApplicationMapper.delete(new QueryWrapper<RegisterApplication>().eq("phone",phone).eq("email",email));
        return CommonResponse.createForSuccess("审核成功，同意注册");
    }

    public CommonResponse<String> RefuseApplication(String phone ,String email){
        registerApplicationMapper.delete(new QueryWrapper<RegisterApplication>().eq("phone",phone).eq("email",email));
        return CommonResponse.createForSuccess("审核成功，拒绝注册");
    }

    public CommonResponse<String> AgreeUpdateAccountInfo(String phone ,String email){
        QueryWrapper<UpdateAccount> queryWrapper = new QueryWrapper<UpdateAccount>().eq("phone", phone).eq("email", email);
        UpdateAccount updateAccount = updateAccountMapper.selectOne(queryWrapper);
        if(updateAccount!=null){
            QueryWrapper<Account> queryWrapper1 = new QueryWrapper<Account>().eq("phone", phone).eq("email", email);
            Account account = accountMapper.selectOne(queryWrapper1);
            account.setAccount_name(updateAccount.getAccount_name());
            account.setEnterprise_type(updateAccount.getEnterprise_type());
            account.setEnterprise_address(updateAccount.getEnterprise_address());
            account.setFile(updateAccount.getFile());
            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("phone",phone).eq("email", email);
            accountMapper.update(account, updateWrapper);
            updateAccountMapper.delete(queryWrapper);
            return CommonResponse.createForSuccess("管理员审核成功，同意修改信息");
        }else return CommonResponse.createForError("管理员审核失败");
    }

    public CommonResponse<String> RefuseUpdateAccountInfo(String phone ,String email){
        QueryWrapper<UpdateAccount> queryWrapper = new QueryWrapper<UpdateAccount>().eq("phone", phone).eq("email", email);
        updateAccountMapper.delete(queryWrapper);
        return CommonResponse.createForSuccess("审核成功，拒绝修改信息");
    }
}
