package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface RegisterApplicationService {
    //企业注册
    CommonResponse<String> EnterpriseUserRegister(MultipartFile file , String account_name , String password , String phone , int enterprise_type , int type );
    //第三方注册
    CommonResponse<String> ThirdPartyRegulatorsRegister(MultipartFile file , String account_name , String password ,  String phone ,  int type );


}
