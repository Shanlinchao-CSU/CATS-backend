package com.example.cntsbackend.util;

import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.RegisterApplication;
import com.example.cntsbackend.persistence.AccountMapper;
import com.example.cntsbackend.persistence.RegisterApplicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenData {
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RegisterApplicationMapper registerApplicationMapper;

    public  void genAccount() throws Exception {
        // 生成数据
        Account account = new Account();
        account.setAccount_name("gen_account"+System.currentTimeMillis());
        account.setPassword(AES.encrypt("123456mm"));
        account.setPhone(AES.encrypt(genPhone()));
        account.setEmail(AES.encrypt(genEmail()));
        account.setType(1);
        account.setFile("file");
        account.setPublic_key(AES.encrypt("0x5B38Da6a701c568545dCfcB11FcB875f56beddC4"));
        account.setEnterprise_type(1);
        account.setSecret_key("2a34575d0f1b7cb39a2c117c0650311a4d3a6e4f507142b45cc3d144bd62ec41");
        account.setT_coin(100);

        // 插入数据
        accountMapper.insert(account);
    }


    public void genRegister() throws Exception {
        // 生成数据
        RegisterApplication registerApplication = new RegisterApplication();
        registerApplication.setAccount_name("gen_account"+System.currentTimeMillis());
        registerApplication.setPassword(AES.encrypt("123456mm"));
        registerApplication.setPhone(AES.encrypt(genPhone()));
        registerApplication.setEmail(AES.encrypt(genEmail()));
        registerApplication.setType(1);
        registerApplication.setFile_address("file");
        registerApplication.setPublic_key(AES.encrypt("0x5B38Da6a701c568545dCfcB11FcB875f56beddC4"));
        registerApplication.setEnterprise_type(1);
        registerApplication.setConductor_id(1);
        registerApplication.setState(0);


        // 插入数据
        registerApplicationMapper.insert(registerApplication);
    }


    // 随机生成手机号
    public static String genPhone() {
        String[] telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
        int index=(int)(Math.random()*telFirst.length);
        String first=telFirst[index];
        String second=String.valueOf((int)(Math.random()*100000000+100000000)).substring(1);
        return first+second;
    }

    // 随机生成邮箱
    public static String genEmail() {
        String[] email_suffix="@qq.com,@163.com,@126.com,@sina.com,@sohu.com,@yahoo.com,@gmail.com,@hotmail.com".split(",");
        int index=(int)(Math.random()*email_suffix.length);
        String suffix=email_suffix[index];
        String first=String.valueOf((int)(Math.random()*100000000+100000000)).substring(1);
        return first+suffix;
    }
}
