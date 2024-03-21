package com.example.cntsbackend.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    public static String encrypt(String input) {
        try {
            // 创建MD5消息摘要对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            String salt = "abckkkkmijs";
            // 将输入字符串和盐值拼接为新的字符串
            String saltedInput = input + salt;
            // 将拼接后的字符串转换为字节数组
            byte[] inputBytes = saltedInput.getBytes();
            // 计算MD5摘要
            byte[] hashedBytes = md.digest(inputBytes);

            // 将字节数组转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // 处理异常情况
            e.printStackTrace();
            return null;
        }
    }
}

