package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;

import java.io.File;

public interface RegisterApplicationService {
    //注册
    CommonResponse<String> register(File file , String name , String password , String phone , String email , int enterprise_type , int type );

}
