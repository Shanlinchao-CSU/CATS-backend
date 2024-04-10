package com.example.cntsbackend.service;

import com.example.cntsbackend.common.CommonResponse;
import com.example.cntsbackend.domain.Account;
import com.example.cntsbackend.domain.RegisterApplication;
import com.example.cntsbackend.domain.UpdateAccount;
import com.example.cntsbackend.dto.AccountDto;
import com.example.cntsbackend.dto.BlockInfoDto;

import java.util.List;
import java.util.Map;

public interface AccountService {

    //----------------------------------企业用户--------------------------------

    //发送手机验证码
    public CommonResponse<String> sendVerificationCodeByPhone(String phoneNumber) throws Exception;
    //发送邮箱验证码
    public CommonResponse<String> sendVerificationCodeByEmail(String email) throws Exception;
    //手机登录
    public CommonResponse<Map> loginByPhone(String phone) throws Exception;
    //邮箱登录
    public CommonResponse<Map> loginByEmail(String email) throws Exception;
    //ID或邮箱或手机+密码登录
    CommonResponse<Map> loginById(String str,String password) throws Exception;
    //修改密码
    CommonResponse<String> changePassword(String phone,String password) throws Exception;
    //验证新手机号并发送验证码(修改手机必须先验证，验证通过并且验证码输入正确后再调用下面的changePhone())
    CommonResponse<String> VerifyNewPhone(String phone) throws Exception;
    //验证新邮箱并发送验证码(修改邮箱必须先验证，验证通过并且验证码输入正确后调用下面的changeEmail())
    CommonResponse<String> VerifyNewEmail(String email) throws Exception;
    //修改手机号
    CommonResponse<String> changePhone(String phone,int account_id) throws Exception;
    //修改邮箱
    CommonResponse<String> changeEmail(String email,int account_id) throws Exception;
    //用户修改个人信息(只能修改name、企业type并提供证明材料)(需要管理员审核)
    CommonResponse<String> updateAccountInfo(Account account);
    //用户查看提交的修改记录
    CommonResponse<UpdateAccount> GetMyUpdateAccountInfo(int account_id);
    //找回密码(使用手机号或邮箱号)
    CommonResponse<String> findPassword(String str,String password) throws Exception;
    //验证数字签名
    CommonResponse<Boolean> verifyDigitalSignature(String signature, String message, String address);
    //验证输入的手机验证码是否正确
    CommonResponse<Boolean> VerifyPhoneCode(String phoneNumber,String code);
    //验证输入的邮箱验证码是否正确
    CommonResponse<Boolean> VerifyEmailCode(String email, String code);
    //(废弃)后端获取区块链相关信息(公钥,如果存在,直接return,不存在则会添加到数据库中)
//    CommonResponse<String> getInfo(int account_id,String public_key) throws Exception;
    //后端获取区块链相关信息(碳币、剩余额度,每次登录都要进行数据更新)
    CommonResponse<String> getT_coinAndT_limit(List<BlockInfoDto> blockInfoDto);
    //后端获取区块链相关信息(碳额度,每次登录都要进行数据更新)
    CommonResponse<String> getT_limit(int account_id, double t_limit);

    //-----------------------------------管理员--------------------------------------

    //同意注册
    CommonResponse<String> AgreeApplication(int register_application_id, int account_id, int amount) throws Exception;
    //拒绝注册
    CommonResponse<String> RefuseApplication(int register_application_id, int account_id);
    //同意修改个人信息
    CommonResponse<String> AgreeUpdateAccountInfo(int account_id);
    //拒绝修改个人信息
    CommonResponse<String> RefuseUpdateAccountInfo(int account_id);
    //管理员获取所有还未被审核的注册申请
    CommonResponse<List<RegisterApplication>> getPendingReviewAccount() throws Exception;
    //管理员获取所有企业用户
    CommonResponse<List<AccountDto>> getAllEnterpriseUsers() throws Exception;
    //管理员修改企业额度
    CommonResponse<String> ModifyT_limit(int account_id , double t_limit);
    //管理员获取各月超额企业信息
    CommonResponse<List<AccountDto>> GetAllExcessEnterprises() throws Exception;
    //根据公钥获取企业信息
    CommonResponse<List<AccountDto>> getEnterpriseInfoByAddress(List<String> publicKey) throws Exception;
}
