package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.RegisterApplication;
import com.example.cntsbackend.persistence.AccountMapper;
import com.example.cntsbackend.persistence.RegisterApplicationMapper;
import com.example.cntsbackend.service.RegisterApplicationService;
import com.example.cntsbackend.util.AES;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class RegisterApplicationServiceImpl implements RegisterApplicationService {
    @Autowired
    private RegisterApplicationMapper registerApplicationMapper;
    @Autowired
    private AccountMapper accountMapper;
    private static final String KEY = "asdasdefsgrfsrdgsfgvsder";
    public CommonResponse<String> EnterpriseUserRegister(File file , String account_name , String password , String phone , int enterprise_type , int type ,String public_key) throws Exception {
        //检验手机是否已被提交注册过
        RegisterApplication registerApplication = registerApplicationMapper.selectOne(new QueryWrapper<RegisterApplication>().eq("phone", phone));
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
        String str= String.valueOf(file);
        // 保存文件到路径
        if(registerApplication!=null || account!=null){
            return CommonResponse.createForError();
        }else {
            phone = AES.encrypt(phone, KEY.getBytes());
            password = AES.encrypt(password, KEY.getBytes());
            public_key = AES.encrypt(public_key, KEY.getBytes());
            RegisterApplication registerApplication1 = new RegisterApplication(account_name,password,phone,type,str,enterprise_type,public_key);
            int i = registerApplicationMapper.insert(registerApplication1);
            return CommonResponse.createForSuccess("SUCCESS");
        }
    }
    public CommonResponse<String> ThirdPartyRegulatorsRegister(File file ,String account_name ,String password ,String phone ,int type ) throws Exception {
        RegisterApplication registerApplication = registerApplicationMapper.selectOne(new QueryWrapper<RegisterApplication>().eq("phone", phone));
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().eq("phone", phone));
        String str= String.valueOf(file);
        if(registerApplication!=null || account!=null){
            return CommonResponse.createForError();
        }else {
            phone = AES.encrypt(phone, KEY.getBytes());
            password = AES.encrypt(password, KEY.getBytes());
            //100为企业类型之外的编号
            RegisterApplication registerApplication1 = new RegisterApplication(account_name,password,phone,type,str,100);
            int i = registerApplicationMapper.insert(registerApplication1);
            return CommonResponse.createForSuccess("SUCCESS");
        }
    }
    public void getRegisterSupportingMaterial(int id, HttpServletResponse response) throws Exception{
        RegisterApplication registerApplication = registerApplicationMapper.selectOne(new QueryWrapper<RegisterApplication>().eq("register_application_id", id));
        String file_address = registerApplication.getFile_address();
        String account_name = registerApplication.getAccount_name();
        String fileName = account_name+"注册证明材料";
        try (FileInputStream inputStream = new FileInputStream(file_address);
             OutputStream outputStream = response.getOutputStream()) {
            byte[] data = new byte[9216];
            // 全文件类型（传什么文件返回什么文件流）
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setHeader("Accept-Ranges", "bytes");
            int read;
            while ((read = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, read);
            }
            // 将缓存区数据进行输出
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("失败"+e);
        }
    }
}
