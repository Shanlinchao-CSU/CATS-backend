package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;

import java.io.File;

public interface RegisterApplicationService {
    //企业注册
    CommonResponse<String> EnterpriseUserRegister(File file , String name , String password , String phone , String email , int enterprise_type , int type );
    //第三方注册
    CommonResponse<String> ThirdPartyRegulatorsRegister(File file , String name , String password ,  String phone ,String email ,  int type );


}
