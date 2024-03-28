package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.CMessage;
import com.example.cntsbackend.domain.RegisterApplication;
import com.example.cntsbackend.domain.UpdateAccount;
import com.example.cntsbackend.dto.AccountDto;
import com.example.cntsbackend.persistence.AccountMapper;
import com.example.cntsbackend.persistence.CMessageMapper;
import com.example.cntsbackend.persistence.RegisterApplicationMapper;
import com.example.cntsbackend.persistence.UpdateAccountMapper;
import com.example.cntsbackend.service.AccountService;
import com.example.cntsbackend.service.RedisService;
import com.example.cntsbackend.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
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
    private static final String CHARACTERS = "0123456789";
    private static final int CODE_LENGTH = 4;

    //--------------------------------------------企业用户--------------------------------------------------

    //检验号码是否已经注册
    public int checkPhoneNumberExist(String phoneNumber) {
        phoneNumber = MD5Util.encrypt(phoneNumber);
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phoneNumber));
        if (account != null) {
            return 0; // 号码已存在，发送验证码
        }
        return 1; // 号码不存在，发送错误信息(号码未被注册)
    }
    //检验邮箱是否已经注册
    public int checkEmailExist(String email) {
        email = MD5Util.encrypt(email);
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
    public CommonResponse<String> sendVerificationCodeByEmail(String email) {
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
    public CommonResponse<Map> loginByEmail(String email){
        email = MD5Util.encrypt(email);
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("email", email));
        if(account!=null){
            Integer enterprise_type = account.getEnterprise_type();
            String cnType = getCNType(enterprise_type);
            Map<String, Object> map = new HashMap<>();
            String token = UUID.randomUUID().toString();
            account.setSecret_key("");
            account.setPublic_key("");
            AccountDto accountDto = new AccountDto(account.getAccount_name(),account.getPassword(),account.getPhone(),account.getEmail(),account.getType(),account.getFile(),account.getT_coin(),cnType);
            map.put("Account",accountDto);
            map.put("token",token);
            redisService.setToken(token,account.getAccount_id());
            return CommonResponse.createForSuccess("邮箱登录成功",map);
        }else return CommonResponse.createForError("邮箱登录失败");
    }
    //手机号码登录
    public CommonResponse<Map> loginByPhone(String phone){
        phone = MD5Util.encrypt(phone);
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
        if(account!=null){
            Integer enterprise_type = account.getEnterprise_type();
            String cnType = getCNType(enterprise_type);
            Map<String, Object> map = new HashMap<>();
            String token = UUID.randomUUID().toString();
            account.setSecret_key("");
            account.setPublic_key("");
            AccountDto accountDto = new AccountDto(account.getAccount_name(),account.getPassword(),account.getPhone(),account.getEmail(),account.getType(),account.getFile(),account.getT_coin(),cnType);
            map.put("Account",accountDto);
            map.put("token",token);
            redisService.setToken(token,account.getAccount_id());
            return CommonResponse.createForSuccess("手机号码登录成功",map);
        }else return CommonResponse.createForError("手机号码登录失败");
    }
    public CommonResponse<Map> loginById(String str,String password){
        String encrypt = MD5Util.encrypt(str);
        password = MD5Util.encrypt(password);
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("account_id", str)
                        .or().eq("email", encrypt)
                        .or().eq("phone", encrypt))
                .eq("password", password);
        Account account = accountMapper.selectOne(queryWrapper);
        if(account!=null){
            Integer enterprise_type = account.getEnterprise_type();
            String cnType = getCNType(enterprise_type);
            Map<String, Object> map = new HashMap<>();
            String token = UUID.randomUUID().toString();
            account.setSecret_key("");
            account.setPublic_key("");
            AccountDto accountDto = new AccountDto(account.getAccount_name(),account.getPassword(),account.getPhone(),account.getEmail(),account.getType(),account.getFile(),account.getT_coin(),cnType);
            map.put("Account",accountDto);
            map.put("token",token);
            redisService.setToken(token,account.getAccount_id());
            return CommonResponse.createForSuccess("id+密码登录成功",map);
        }else return CommonResponse.createForError("id+密码登录失败");
    }

    public CommonResponse<String> changePassword(String phone,String password){
        phone = MD5Util.encrypt(phone);
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
        if(account!=null){
            password = MD5Util.encrypt(password);
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
        email = MD5Util.encrypt(email);
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("email", email));
        if(account!=null){
            phone = MD5Util.encrypt(phone);
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
        phone = MD5Util.encrypt(phone);
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
        if(account!=null){
            email = MD5Util.encrypt(email);
            account.setEmail(email);

            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("phone",phone);
            accountMapper.update(account, updateWrapper);

            return CommonResponse.createForSuccess("修改邮箱成功");
        }else return CommonResponse.createForError("用户不存在，修改邮箱失败");
    }
    public CommonResponse<String> updateAccountInfo(Account account){
        String password = MD5Util.encrypt(account.getPassword());
        String phone = MD5Util.encrypt(account.getPhone());
        String email = MD5Util.encrypt(account.getEmail());
        UpdateAccount updateAccount = new UpdateAccount(account.getAccount_name(),password,phone,email,account.getType(),account.getEnterprise_type(),0,account.getFile());
        updateAccountMapper.insert(updateAccount);
        return CommonResponse.createForSuccess("提交修改信息成功，等待审核");
    }

    public CommonResponse<String> findPassword(String str,String password){
        str = MD5Util.encrypt(str);
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", str).or().eq("email", str));
        if(account!=null) {
            password = MD5Util.encrypt(password);
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
        }
        return cnType;
    }

    public CommonResponse<String> getInfo(int account_id,String public_key,String secret_key){
        if(public_key != null && secret_key != null){
            return CommonResponse.createForError("已获取过该用户信息");
        }else {
            Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", account_id));
            public_key = MD5Util.encrypt(public_key);
            secret_key = MD5Util.encrypt(secret_key);
            account.setPublic_key(public_key);
            account.setSecret_key(secret_key);
            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("account_id",account_id);
            accountMapper.update(account,updateWrapper);
            return CommonResponse.createForSuccess("获取用户信息成功");
        }
    }

    public CommonResponse<String> getT_coinAndT_limit(int account_id, double t_coin, double t_remain,double t_limit){
        // 获取当前时间的上个月份
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        // 格式化为"yyyy-MM"的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String previousMonthString = previousYearMonth.format(formatter);
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_id", account_id));
        account.setT_coin(t_coin);
        UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("account_id",account_id);
        accountMapper.update(account,updateWrapper);
        CMessage cMessage = cMessageMapper.selectOne(new QueryWrapper<CMessage>().eq("account_id", account_id).eq("month",previousMonthString));
        if(cMessage != null){
            cMessage.setT_remain(t_remain);
            cMessage.setT_limit(t_limit);
            UpdateWrapper<CMessage> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("account_id",account_id);
            cMessageMapper.update(cMessage,updateWrapper1);
            return CommonResponse.createForSuccess("获取用户碳币信息成功");
        }else {
            return CommonResponse.createForError("获取信息失败");
        }

    }


    //-------------------------------------------管理员------------------------------------------------------

    public CommonResponse<String> AgreeApplication(int register_application_id, int account_id) throws NoSuchAlgorithmException {
        RegisterApplication registerApplication = registerApplicationMapper.selectOne(new QueryWrapper<RegisterApplication>().eq("register_application_id", register_application_id));
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
        byte[] bytes = AES.generateKey(public_key);
        String secret_key = Arrays.toString(bytes);
        password = MD5Util.encrypt(password);
        phone = MD5Util.encrypt(phone);
        email = MD5Util.encrypt(email);
        public_key = MD5Util.encrypt(public_key);
        secret_key = MD5Util.encrypt(secret_key);
        accountMapper.insert(new Account(account_name, password, phone, email, type,enterprise_type,public_key,file_address,secret_key));
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
        int account_id1 = account.getAccount_id();
        // 获取当前年月
        YearMonth yearMonth = YearMonth.now();
        // 定义日期时间格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // 格式化为指定格式的字符串
        String month = yearMonth.format(formatter);

        //TODO:确定cmessage表碳额度的初始值以及碳币,要与以太坊联系起来(用定时任务来完成cMessage的更新)
        cMessageMapper.insert(new CMessage(account_id1,500,null,500));
        return CommonResponse.createForSuccess("审核成功，同意注册");
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

    public CommonResponse<String> AgreeUpdateAccountInfo(String phone ,String email){
        QueryWrapper<UpdateAccount> queryWrapper = new QueryWrapper<UpdateAccount>().eq("phone", phone).eq("email", email);
        UpdateAccount updateAccount = updateAccountMapper.selectOne(queryWrapper);
        if(updateAccount!=null){
            QueryWrapper<Account> queryWrapper1 = new QueryWrapper<Account>().eq("phone", phone).eq("email", email);
            Account account = accountMapper.selectOne(queryWrapper1);
            account.setAccount_name(updateAccount.getAccount_name());
            account.setEnterprise_type(updateAccount.getEnterprise_type());
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
    public CommonResponse<List<RegisterApplication>> getPendingReviewAccount(){
        List<RegisterApplication> registerApplications = registerApplicationMapper.selectList(new QueryWrapper<RegisterApplication>().eq("state", 0));
        return CommonResponse.createForSuccess("查询未审核申请信息成功",registerApplications);
    }
    public CommonResponse<List<Account>> getAllEnterpriseUsers(){
        List<Account> accountList = accountMapper.selectList(new QueryWrapper<Account>().eq("type",1));
        return CommonResponse.createForSuccess("获取所有企业用户成功",accountList);
    }
}
