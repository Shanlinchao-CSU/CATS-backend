package com.example.cntsbackend.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.log4j.Log4j2;

/**
 * @ClassName:SendSmsUtil
 * @Description: 阿里云短信工具类
 * @author: TracyYang
 * @date:2019年8月28日 上午10:15:40
 */
@Log4j2
public class SendPhoneUtil {

    // 签名
    private static final String signName = "碳碳交易";

    // 模板
    private static final String templateCode = "SMS_465365256";

    // 阿里云短信配置信息
    private static final String accessKeyId = "LTAI5tNx3htyeZaGm4BVaizh";
    private static final String accessKeySecret = "gMmbxjNaLeGjl7p8YWl8as38K297ay";
    private static final String REGION_ID = "cn-hangzhou";
    private static final String PRODUCT = "Dysmsapi";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    /**
     * 发送短信通知
     *
     * @param mobile 手机号
     * @param code 验证码
     * @return 执行结果
     */
    public static boolean sendSMS(String mobile, String code) {
        try {
            IClientProfile profile = DefaultProfile.getProfile(REGION_ID, accessKeyId, accessKeySecret);

            DefaultProfile.addEndpoint(REGION_ID, REGION_ID, PRODUCT, DOMAIN);

            IAcsClient acsClient = new DefaultAcsClient(profile);

            SendSmsRequest request = new SendSmsRequest();

            request.setMethod(MethodType.POST);

            request.setPhoneNumbers(mobile);

            request.setSignName(signName);

            request.setTemplateCode(templateCode);

            request.setTemplateParam("{code:" + code + "}");

            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if ((sendSmsResponse.getCode() != null) && (sendSmsResponse.getCode().equals("OK"))) {
                log.info("发送成功,code:" + sendSmsResponse.getCode());
                return true;
            } else {
                log.info("发送失败,code:" + sendSmsResponse.getCode());
                return false;
            }
        } catch (ClientException e) {
            log.error("发送失败,系统错误！", e);
            return false;
        }
    }

//    public static void main(String[] args) {
//        SendPhoneUtil.sendSMS("19313028910","123456");
//    }

}

