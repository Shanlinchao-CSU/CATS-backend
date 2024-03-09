package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;

import java.io.File;

public interface RegisterApplicationService {
    //TODO:(改动，减少企业地址这一个参数)企业注册
    CommonResponse<String> EnterpriseUserRegister(File file ,String account_name ,String password ,String phone ,String email ,int enterprise_type ,int type );
    //第三方注册
    CommonResponse<String> ThirdPartyRegulatorsRegister(File file , String account_name , String password ,  String phone ,String email ,  int type );


}
