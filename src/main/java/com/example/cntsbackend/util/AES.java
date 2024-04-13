package com.example.cntsbackend.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AES {

    private static final String MODULE_NAME = "AES";
    private static final String MODULE_VERSION = "v1.0.0";
    private static final String DEFAULT_KEY = "2a34575d0f1b7cb39a2c117c0650311a4d3a6e4f507142b45cc3d144bd62ec41";


    // 根据默认密钥加密
    public static String encrypt(String text) throws Exception {
        byte[] key = hexToBytes(DEFAULT_KEY);
        return encrypt(text, key);
    }

    // 根据默认密钥解密
    public static String decrypt(String encryptedText) throws Exception {
        byte[] key = hexToBytes(DEFAULT_KEY);
        return decrypt(encryptedText, key);
    }

    // 加密函数
    public static String encrypt(String text, byte[] key) throws Exception {
        text = text.toLowerCase();
        byte[] iv = Arrays.copyOfRange(key, 0, 16);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        byte[] encrypted = cipher.doFinal(text.getBytes());
        return bytesToHex(encrypted);
    }

    // 解密函数
    public static String decrypt(String encryptedText, byte[] key) throws Exception {
        encryptedText = encryptedText.toLowerCase();
        byte[] iv = Arrays.copyOfRange(key, 0, 16);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        byte[] decrypted = cipher.doFinal(hexToBytes(encryptedText));
        return new String(decrypted);
    }

    // 生成密钥
    public static byte[] generateKey(String address) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyA = digest.digest(address.getBytes());
        return Arrays.copyOf(keyA, 32); // AES-256 密钥长度为 32 字节
    }

    // 将字节数组转换为十六进制字符串
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 将十六进制字符串转换为字节数组
    public static byte[] hexToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }


//    public static void main(String[] args) throws Exception {
//        // 地址(公钥)
//        String address = "0x5B38Da6a701c568545dCfcB11FcB875f56beddC4";
//
//        // 使用 address 生成密匙
//        byte[] key = generateKey(address);
//
//        System.out.println("密钥: " + bytesToHex(key));
//
//        // 要加密的数据
//        String originalText = "Hello World! Where are you? I am here!";
//
//        // 加密数据
//        String encryptedText = encrypt(originalText, key);
//        System.out.println("加密后的数据: " + encryptedText);
//        System.out.println("加密数据长度: " + encryptedText.length());
//
//        // 解密数据
//        String decryptedText = decrypt(encryptedText, key);
//        System.out.println("解密后的数据: " + decryptedText);
//    }
}
