package com.example.cntsbackend.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.RegisterApplication;
import com.example.cntsbackend.persistence.RegisterApplicationMapper;
import com.example.cntsbackend.service.RegisterApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class RegisterApplicationServiceImpl implements RegisterApplicationService {
    @Autowired
    private RegisterApplicationMapper registerApplicationMapper;
    public CommonResponse<String> EnterpriseUserRegister(File file ,String account_name ,String password ,String phone ,int enterprise_type ,int type ){
        RegisterApplication registerApplication = registerApplicationMapper.selectOne(new QueryWrapper<RegisterApplication>().eq("phone", phone));
        String str= String.valueOf(file);
        RegisterApplication registerApplication1 = new RegisterApplication(account_name,password,phone,type,str,enterprise_type);
        if(registerApplication!=null){
            return CommonResponse.createForError();
        }else {
            int i = registerApplicationMapper.insert(registerApplication1);
            return CommonResponse.createForSuccess("SUCCESS");
        }
    }
    public CommonResponse<String> ThirdPartyRegulatorsRegister(File file ,String account_name ,String password ,String phone ,int type ){
        RegisterApplication registerApplication = registerApplicationMapper.selectOne(new QueryWrapper<RegisterApplication>().eq("phone", phone));
        String str= String.valueOf(file);
        //100为企业类型之外的编号
        RegisterApplication registerApplication1 = new RegisterApplication(account_name,password,phone,type,str,100);
        if(registerApplication!=null){
            return CommonResponse.createForError();
        }else {
            int i = registerApplicationMapper.insert(registerApplication1);
            return CommonResponse.createForSuccess("SUCCESS");
        }
    }
}
