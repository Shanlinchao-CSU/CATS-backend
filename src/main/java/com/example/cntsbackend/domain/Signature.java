package com.example.cntsbackend.domain;

/**
 * 数字签名传递体
 */
public class Signature {
    // 数字签名
    private String signature;
    // 消息
    private String message;
    // 公钥
    private String address;

    public Signature(String signature, String message, String address) {
        this.signature = signature;
        this.message = message;
        this.address = address;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
