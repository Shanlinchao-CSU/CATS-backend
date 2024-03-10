package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;

import java.io.File;

public interface RegisterApplicationService {
    //TODO:(修改，减少邮箱这个参数)企业注册
    CommonResponse<String> EnterpriseUserRegister(File file ,String account_name ,String password ,String phone ,int enterprise_type ,int type );
    //TODO:(修改，减少邮箱这个参数)第三方注册
    CommonResponse<String> ThirdPartyRegulatorsRegister(File file , String account_name , String password ,  String phone ,  int type );


}
