package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface RegisterApplicationService {
    //企业注册
    CommonResponse<String> EnterpriseUserRegister(File file , String account_name , String password , String phone , int enterprise_type , int type );
    //第三方注册
    CommonResponse<String> ThirdPartyRegulatorsRegister(File file , String account_name , String password ,  String phone ,  int type );
    //管理员获取注册申请的文件流
    void getRegisterSupportingMaterial(int id, HttpServletResponse response) throws Exception;


}
