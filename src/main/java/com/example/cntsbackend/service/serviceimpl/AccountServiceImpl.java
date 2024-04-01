package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.*;
import com.example.cntsbackend.dto.AccountDto;
import com.example.cntsbackend.persistence.*;
import com.example.cntsbackend.service.AccountService;
import com.example.cntsbackend.service.RedisService;
import com.example.cntsbackend.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    @Autowired
    private RedisService redisService;
    @Autowired
    private AccountLimitMapper accountLimitMapper;
    private static final String CHARACTERS = "0123456789";
    private static final int CODE_LENGTH = 4;
    private static final String KEY = "2a34575d0f1b7cb39a2c117c0650311a4d3a6e4f507142b45cc3d144bd62ec41";

    //--------------------------------------------企业用户--------------------------------------------------

    //检验号码是否已经注册
    public int checkPhoneNumberExist(String phoneNumber) throws Exception {
        phoneNumber = AES.encrypt(phoneNumber, AES.hexToBytes(KEY));
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phoneNumber));
        RegisterApplication registerApplication = registerApplicationMapper.selectOne(new QueryWrapper<RegisterApplication>().eq("phone", phoneNumber));
        if (account != null && registerApplication !=null) {
            return 0; // 号码已存在，发送验证码
        }
        return 1; // 号码不存在，发送错误信息(号码未被注册)
    }
    //检验邮箱是否已经注册
    public int checkEmailExist(String email) throws Exception {
        email = AES.encrypt(email, AES.hexToBytes(KEY));
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("email", email));
        RegisterApplication registerApplication = registerApplicationMapper.selectOne(new QueryWrapper<RegisterApplication>().eq("email", email));
        if (account != null && registerApplication !=null) {
            return 0; // 邮箱已存在，发送验证码
        }
        return 1; // 邮箱不存在，发送错误信息(邮箱未被注册)
    }

    //发送手机验证码
    public CommonResponse<String> sendVerificationCodeByPhone(String phoneNumber) throws Exception {
        if(checkPhoneNumberExist(phoneNumber)==0){
            // 生成四位包括数字、小写字母和大写字母的随机验证码
            String verificationCode = generateVerificationCode();
            redisService.XAdd("p"+"+"+phoneNumber+"+"+verificationCode);
            return CommonResponse.createForSuccess("SUCCESS",verificationCode); // 发送成功
        }else{
            return CommonResponse.createForError(1,"手机号不存在");
        }
    }
    public CommonResponse<String> sendVerificationCodeByChangePhone(String phoneNumber){
        String verificationCode = generateVerificationCode();
        redisService.XAdd("p"+"+"+phoneNumber+"+"+verificationCode);
        return CommonResponse.createForSuccess("SUCCESS",verificationCode); // 发送成功
    }
    //发送邮箱验证码
    public CommonResponse<String> sendVerificationCodeByEmail(String email) throws Exception {
        if(checkEmailExist(email)==0){
            // 生成四位包括数字、小写字母和大写字母的随机验证码
            String verificationCode = generateVerificationCode();
            redisService.XAdd("e"+"+"+email+"+"+verificationCode);
            return CommonResponse.createForSuccess("SUCCESS",verificationCode); // 发送成功
        }else{
            return CommonResponse.createForError(1,"邮箱不存在");
        }
    }
    public CommonResponse<String> sendVerificationCodeByChangeEmail(String email){
        String verificationCode = generateVerificationCode();
        redisService.XAdd("e"+"+"+email+"+"+verificationCode);
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
    public CommonResponse<Map> loginByEmail(String email) throws Exception {
        email = AES.encrypt(email, AES.hexToBytes(KEY));
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("email", email));
        if(account!=null){
            Integer enterprise_type = account.getEnterprise_type();
            String cnType = "";
            double t_limit = 0;
            if(enterprise_type!=100){
                cnType = getCNType(enterprise_type);
                int account_id = account.getAccount_id();
                AccountLimit accountLimit = accountLimitMapper.selectOne(new QueryWrapper<AccountLimit>().eq("account_id", account_id));
                t_limit = accountLimit.getT_limit();
            }
            Map<String, Object> map = new HashMap<>();
            String token = UUID.randomUUID().toString();
            String phone = account.getPhone();
            phone = AES.decrypt(phone, AES.hexToBytes(KEY));
            email = AES.decrypt(email, AES.hexToBytes(KEY));
            AccountDto accountDto = new AccountDto(account.getAccount_id(),account.getAccount_name(),phone,email,account.getType(),account.getFile(),account.getT_coin(),cnType,t_limit);
            map.put("Account",accountDto);
            map.put("token",token);
            redisService.setToken(token,account.getAccount_id());
            return CommonResponse.createForSuccess("邮箱登录成功",map);
        }else return CommonResponse.createForError("邮箱登录失败");
    }
    //手机号码登录
    public CommonResponse<Map> loginByPhone(String phone) throws Exception {
        phone = AES.encrypt(phone, AES.hexToBytes(KEY));
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
        if(account!=null){
            Integer enterprise_type = account.getEnterprise_type();
            String cnType = "";
            double t_limit = 0;
            if(enterprise_type!=100){
                cnType = getCNType(enterprise_type);
                int account_id = account.getAccount_id();
                AccountLimit accountLimit = accountLimitMapper.selectOne(new QueryWrapper<AccountLimit>().eq("account_id", account_id));
                t_limit = accountLimit.getT_limit();
            }
            Map<String, Object> map = new HashMap<>();
            String token = UUID.randomUUID().toString();
            phone = AES.decrypt(phone, AES.hexToBytes(KEY));
            String email = account.getEmail();
            if(email != null){
                email = AES.decrypt(email, AES.hexToBytes(KEY));
            }
            AccountDto accountDto = new AccountDto(account.getAccount_id(),account.getAccount_name(),phone,email,account.getType(),account.getFile(),account.getT_coin(),cnType,t_limit);
            map.put("Account",accountDto);
            map.put("token",token);
            redisService.setToken(token,account.getAccount_id());
            return CommonResponse.createForSuccess("手机号码登录成功",map);
        }else return CommonResponse.createForError("手机号码登录失败");
    }
    public CommonResponse<Map> loginById(String str,String password) throws Exception {
        password = AES.encrypt(password, AES.hexToBytes(KEY));
        String encrypt_str = AES.encrypt(str, AES.hexToBytes(KEY));
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("account_id", str)
                        .or().eq("email", encrypt_str)
                        .or().eq("phone", encrypt_str))
                .eq("password", password);
        Account account = accountMapper.selectOne(queryWrapper);
        if(account!=null){
            Integer enterprise_type = account.getEnterprise_type();
            String cnType = "";
            double t_limit = 0;
            if (enterprise_type!=100) {
                cnType=getCNType(enterprise_type);
                int account_id = account.getAccount_id();
                AccountLimit accountLimit = accountLimitMapper.selectOne(new QueryWrapper<AccountLimit>().eq("account_id", account_id));
                t_limit = accountLimit.getT_limit();
            }
            Map<String, Object> map = new HashMap<>();
            String token = UUID.randomUUID().toString();
            String phone = account.getPhone();
            phone = AES.decrypt(phone, AES.hexToBytes(KEY));
            String email = account.getEmail();
            if(email != null){
                email = AES.decrypt(email, AES.hexToBytes(KEY));
            }
            AccountDto accountDto = new AccountDto(account.getAccount_id(),account.getAccount_name(),phone,email,account.getType(),account.getFile(),account.getT_coin(),cnType,t_limit);
            map.put("Account",accountDto);
            map.put("token",token);
            redisService.setToken(token,account.getAccount_id());
            return CommonResponse.createForSuccess("id+密码登录成功",map);
        }else return CommonResponse.createForError("id+密码登录失败");
    }

    public CommonResponse<String> changePassword(String phone,String password) throws Exception {
        phone = AES.encrypt(phone, AES.hexToBytes(KEY));
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
        if(account!=null){
            password = AES.encrypt(password, AES.hexToBytes(KEY));
            account.setPassword(password);

            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("phone",phone);
            accountMapper.update(account, updateWrapper);

            return CommonResponse.createForSuccess("修改密码成功");
        }else return CommonResponse.createForError("用户不存在，修改密码失败");
    }
    public CommonResponse<String> VerifyNewPhone(String phone) throws Exception {
        if(checkPhoneNumberExist(phone)==0){
            return CommonResponse.createForError("手机号已被使用");
        }else{
            return sendVerificationCodeByChangePhone(phone);
        }
    }
    public CommonResponse<String> changePhone(String phone,int account_id) throws Exception {
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", account_id));
        if(account!=null){
            phone = AES.encrypt(phone, AES.hexToBytes(KEY));
            account.setPhone(phone);

            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("account_id",account_id);
            accountMapper.update(account, updateWrapper);

            return CommonResponse.createForSuccess("修改手机号成功");
        }else return CommonResponse.createForError("用户不存在，修改手机号失败");
    }
    public CommonResponse<String> VerifyNewEmail(String email) throws Exception {
        if(checkEmailExist(email)==0){
            return CommonResponse.createForError("邮箱已被使用");
        }else{
            return sendVerificationCodeByChangeEmail(email);
        }
    }
    public CommonResponse<String> changeEmail(String email,int account_id) throws Exception {
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", account_id));
        if(account!=null){
            email = AES.encrypt(email, AES.hexToBytes(KEY));
            account.setEmail(email);

            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("account_id",account_id);
            accountMapper.update(account, updateWrapper);

            return CommonResponse.createForSuccess("修改邮箱成功");
        }else return CommonResponse.createForError("用户不存在，修改邮箱失败");
    }
    public CommonResponse<String> updateAccountInfo(Account account){
        int account_id = account.getAccount_id();
        UpdateAccount updateAccount1 = updateAccountMapper.selectOne(new QueryWrapper<UpdateAccount>().eq("account_id", account_id));
        if(updateAccount1 !=null){
            updateAccountMapper.deleteById(updateAccount1);
        }else {
            UpdateAccount updateAccount = new UpdateAccount(account.getAccount_id(),account.getAccount_name(),account.getEnterprise_type(),account.getFile());
            updateAccountMapper.insert(updateAccount);
        }
        return CommonResponse.createForSuccess("提交修改信息成功，等待审核");
    }

    public CommonResponse<String> findPassword(String str,String password) throws Exception {
        str = AES.encrypt(str, AES.hexToBytes(KEY));
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", str).or().eq("email", str));
        if(account!=null) {
            password = AES.encrypt(password, AES.hexToBytes(KEY));
            account.setPassword(password);
            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("phone", str).or().eq("email", str);
            accountMapper.update(account,updateWrapper);
            return CommonResponse.createForSuccess("找回密码成功");
        }else return CommonResponse.createForError("手机号或邮箱不存在");
    }

    /**
     * 验证数字签名
     * @param signature 数字签名
     * @param message 消息
     * @param address 钱包地址
     * @return 是否验证成功
     */
    public CommonResponse<Boolean> verifyDigitalSignature(String signature, String message, String address){
        return MetaMaskUtil.validate(signature, message, address)
                ? CommonResponse.createForSuccess("验证成功", true)
                : CommonResponse.createForSuccess("验证失败", false);
    }

    public CommonResponse<Boolean> VerifyPhoneCode(String phoneNumber,String code){
        String o = (String) redisService.get(phoneNumber);
        if(code.equals(o)){
            return CommonResponse.createForSuccess("验证成功",true);
        }else return CommonResponse.createForSuccess("验证失败",false);
    }

    public CommonResponse<Boolean> VerifyEmailCode(String email,String code){
        String o = redisService.get(email).toString();
        if(code.equals(o)){
            return CommonResponse.createForSuccess("验证成功",true);
        }else return CommonResponse.createForSuccess("验证失败",false);
    }

    public String getCNType(int enterprise_type){
        String cnType = "";
        switch (enterprise_type) {
            case 0 -> cnType="发电企业";
            case 1 -> cnType="电网企业";
            case 2 -> cnType="钢铁生产企业";
            case 3 -> cnType="化工生产企业";
            case 4 -> cnType="电解铝生产企业企业";
            case 5 -> cnType="镁冶炼企业";
            case 6 -> cnType="平板玻璃生产企业";
            case 7 -> cnType="水泥生产企业";
            case 8 -> cnType="陶瓷生产企业";
            case 9 -> cnType="民航企业";
            case 10 -> cnType="其它企业";
            default -> cnType="";
        }
        return cnType;
    }

//    public CommonResponse<String> getInfo(int account_id,String public_key) throws Exception {
//        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", account_id));
//        if(account.getPublic_key() != null){
//            return CommonResponse.createForError("已获取过该用户信息");
//        }else {
//            public_key = AES.encrypt(public_key, KEY.getBytes());
//            account.setPublic_key(public_key);
//            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
//            updateWrapper.eq("account_id",account_id);
//            accountMapper.update(account,updateWrapper);
//            return CommonResponse.createForSuccess("获取用户信息成功");
//        }
//    }

    public CommonResponse<String> getT_coinAndT_limit(int account_id, double t_coin, double t_remain,double t_limit){
        // 获取当前时间的上个月份
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        // 格式化为"yyyy-MM"的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String previousMonthString = previousYearMonth.format(formatter);
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", account_id));
        //更新用户表中的碳币
        account.setT_coin(t_coin);
        UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("account_id",account_id);
        accountMapper.update(account,updateWrapper);
        //更新用户的额度
        AccountLimit accountLimit = accountLimitMapper.selectOne(new QueryWrapper<AccountLimit>().eq("account_id", account_id));
        accountLimit.setT_limit(t_limit);
        //更新用户剩余额度
        CMessage cMessage = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", account_id).eq("month",previousMonthString));
        if(cMessage != null){
            cMessage.setT_remain(t_remain);
            UpdateWrapper<CMessage> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("account_id",account_id);
            cMessageMapper.update(cMessage,updateWrapper1);
            return CommonResponse.createForSuccess("获取用户碳币信息成功");
        }else {
            return CommonResponse.createForError("该用户还未进行碳核算");
        }

    }


    //-------------------------------------------管理员------------------------------------------------------

    public CommonResponse<String> AgreeApplication(int register_application_id, int account_id) throws Exception {
        RegisterApplication registerApplication = registerApplicationMapper.selectOne(new QueryWrapper<RegisterApplication>().eq("register_application_id", register_application_id));
        if(registerApplication != null){
            registerApplication.setConductor_id(account_id);
            registerApplication.setState(1);
            UpdateWrapper<RegisterApplication> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("register_application_id", register_application_id);
            registerApplicationMapper.update(registerApplication, updateWrapper);
            String account_name = registerApplication.getAccount_name();
            String password = registerApplication.getPassword();
            String phone = registerApplication.getPhone();
            String email = registerApplication.getEmail();
            int enterprise_type = registerApplication.getEnterprise_type();
            String file_address = registerApplication.getFile_address();
            Integer type = registerApplication.getType();
            String public_key = registerApplication.getPublic_key();
            String secret_key = "";
            if(public_key != null){
                byte[] bytes = AES.generateKey(public_key);
                secret_key = Arrays.toString(bytes);
            }
            accountMapper.insert(new Account(account_name, password, phone, email, type,enterprise_type,public_key,file_address,secret_key));
            if(type == 1){
                Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
                accountLimitMapper.insert(new AccountLimit(account.getAccount_id(),500,500));
            }
            return CommonResponse.createForSuccess("审核成功，同意注册");
        }else return CommonResponse.createForError("该请求注册id不存在");
//        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
//        int account_id1 = account.getAccount_id();
//        // 获取当前年月
//        YearMonth yearMonth = YearMonth.now();
//        // 定义日期时间格式化器
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
//        // 格式化为指定格式的字符串
//        String month = yearMonth.format(formatter);

    }

    public CommonResponse<String> RefuseApplication(int register_application_id, int account_id){
        RegisterApplication registerApplication = registerApplicationMapper.selectOne(new QueryWrapper<RegisterApplication>().eq("register_application_id", register_application_id));
        registerApplication.setConductor_id(account_id);
        registerApplication.setState(2);
        UpdateWrapper<RegisterApplication> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("register_application_id", register_application_id);
        registerApplicationMapper.update(registerApplication, updateWrapper);
        return CommonResponse.createForSuccess("审核成功，拒绝注册");
    }

    public CommonResponse<String> AgreeUpdateAccountInfo(int account_id){
        QueryWrapper<UpdateAccount> queryWrapper = new QueryWrapper<UpdateAccount>().eq("account_id", account_id);
        UpdateAccount updateAccount = updateAccountMapper.selectOne(queryWrapper);
        if(updateAccount!=null){
            QueryWrapper<Account> queryWrapper1 = new QueryWrapper<Account>().eq("account_id", account_id);
            Account account = accountMapper.selectOne(queryWrapper1);
            account.setAccount_name(updateAccount.getAccount_name());
            account.setEnterprise_type(updateAccount.getEnterprise_type());
            account.setFile(updateAccount.getFile());
            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("account_id", account_id);
            accountMapper.update(account, updateWrapper);
            updateAccountMapper.delete(queryWrapper);
            return CommonResponse.createForSuccess("管理员审核成功，同意修改信息");
        }else return CommonResponse.createForError("管理员审核失败");
    }

    public CommonResponse<String> RefuseUpdateAccountInfo(int account_id){
        QueryWrapper<UpdateAccount> queryWrapper = new QueryWrapper<UpdateAccount>().eq("account_id", account_id);
        updateAccountMapper.delete(queryWrapper);
        return CommonResponse.createForSuccess("审核成功，拒绝修改信息");
    }
    public CommonResponse<List<RegisterApplication>> getPendingReviewAccount() throws Exception {
        List<RegisterApplication> registerApplications = registerApplicationMapper.selectList(new QueryWrapper<RegisterApplication>().eq("state", 0));
        for (RegisterApplication registerApplication : registerApplications) {
            String phone = registerApplication.getPhone();
            phone = AES.decrypt(phone, AES.hexToBytes(KEY));
            registerApplication.setPhone(phone);
            registerApplication.setPassword("");
            registerApplication.setPublic_key("");
        }
        return CommonResponse.createForSuccess("查询未审核申请信息成功",registerApplications);
    }
    public CommonResponse<List<AccountDto>> getAllEnterpriseUsers() throws Exception {
        List<Account> accountList = accountMapper.selectList(new QueryWrapper<Account>().eq("type",1));
        List<AccountDto> accountDtoList = new ArrayList<>();
        for (Account account : accountList) {
            int account_id = account.getAccount_id();
            AccountLimit accountLimit = accountLimitMapper.selectOne(new QueryWrapper<AccountLimit>().eq("account_id", account_id));
            double t_limit = accountLimit.getT_limit();
            Integer enterprise_type = account.getEnterprise_type();
            String cnType = "";
            if(enterprise_type!=100){
                cnType = getCNType(enterprise_type);
            }
            String phone = account.getPhone();
            phone = AES.decrypt(phone, AES.hexToBytes(KEY));
            String email = account.getEmail();
            if(email !=null){
                email = AES.decrypt(email, AES.hexToBytes(KEY));
            }
            AccountDto accountDto = new AccountDto(account_id,account.getAccount_name(),phone,email,account.getType(),account.getFile(),account.getT_coin(),cnType,t_limit);
            accountDtoList.add(accountDto);
        }
        return CommonResponse.createForSuccess("获取所有企业用户成功",accountDtoList);
    }

    public CommonResponse<String> ModifyT_limit(int account_id , double t_limit){
        AccountLimit accountLimit = accountLimitMapper.selectOne(new QueryWrapper<AccountLimit>().eq("account_id", account_id));
        accountLimit.setT_next_month(t_limit);
        UpdateWrapper<AccountLimit> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("account_id",account_id);
        accountLimitMapper.update(accountLimit,updateWrapper);
        return CommonResponse.createForSuccess("修改额度成功");
    }

    public CommonResponse<List<AccountDto>> GetAllExcessEnterprises() throws Exception {
        List<CMessage> cMessageList = cMessageMapper.selectList(new QueryWrapper<CMessage>().lt("t_remain", 0));
        List<AccountDto> accountDtoList = new ArrayList<>();
        for (CMessage cMessage : cMessageList) {
            int account_id = cMessage.getAccount_id();
            double t_remain = cMessage.getT_remain();
            double t_limit = cMessage.getT_limit();
            Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", account_id));
            String email = account.getEmail();
            if (email == null) {
                email = "无";
            }else email = AES.decrypt(email, AES.hexToBytes(KEY));
            Integer enterprise_type = account.getEnterprise_type();
            String cnType = "";
            if (enterprise_type != 100) {
                cnType = getCNType(enterprise_type);
            }
            String phone = account.getPhone();
            phone = AES.decrypt(phone, AES.hexToBytes(KEY));
            AccountDto accountDto = new AccountDto(account_id, account.getAccount_name(), phone, email, cnType, cMessage.getMonth(), t_remain,t_limit);
            accountDtoList.add(accountDto);
        }
        return CommonResponse.createForSuccess("获取各月超额企业信息成功",accountDtoList);
    }
}
