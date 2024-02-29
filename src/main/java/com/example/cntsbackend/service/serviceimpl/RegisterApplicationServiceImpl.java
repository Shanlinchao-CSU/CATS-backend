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
    public CommonResponse<String> register(File file ,String name ,String password ,String phone ,String email ,int enterprise_type ,int type ){
        RegisterApplication registerApplication = registerApplicationMapper.selectOne(new QueryWrapper<RegisterApplication>().eq("phone", phone).eq("email", email));
        String str= String.valueOf(file);
        RegisterApplication registerApplication1 = new RegisterApplication(str,name,password,phone,email,enterprise_type,type);
        if(registerApplication!=null){
            return CommonResponse.createForError();
        }else {
            int i = registerApplicationMapper.insert(registerApplication1);
            return CommonResponse.createForSuccess("SUCCESS");
        }
    }
}
