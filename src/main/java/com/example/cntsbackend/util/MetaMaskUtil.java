package com.example.cntsbackend.util;

import org.web3j.crypto.*;
import org.web3j.utils.Numeric;
import org.web3j.crypto.Sign.SignatureData;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * 以太坊签名消息校验工具
 */
public class MetaMaskUtil {
    /**
     * 以太坊自定义的签名消息都以以下字符开头
     * 参考 eth_sign in https://github.com/ethereum/wiki/wiki/JSON-RPC
     */
    public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    public static void main(String[] args) {
        //签名后的数据
        String signature="0x7d6216fde3888c5e54d4f1a54f9e8f54fdc83e4a10f105e57defd4c6d50996ba1503cffae5aedc14681c6189303fabeed7c336ab1383745dd9ca82f1916593151c";
        //签名原文
        String message="数字签名";
        //签名的钱包地址
        String address="0xBF1c740663F2f2ec301898a0d34339520247D189";
        Boolean result = validate(signature,message,address);
        System.out.println(result);
    }
    /**
     * 对签名消息，原始消息，账号地址三项信息进行认证，判断签名是否有效
     * @param signature
     * @param message
     * @param address
     * @return
     */
    public static boolean validate(String signature, String message, String address) {
        //参考 eth_sign in https://github.com/ethereum/wiki/wiki/JSON-RPC
        // eth_sign
        // The sign method calculates an Ethereum specific signature with:
        //    sign(keccak256("\x19Ethereum Signed Message:\n" + len(message) + message))).
        //
        // By adding a prefix to the message makes the calculated signature recognisable as an Ethereum specific signature.
        // This prevents misuse where a malicious DApp can sign arbitrary data (e.g. transaction) and use the signature to
        // impersonate the victim.
        String prefix = PERSONAL_MESSAGE_PREFIX + message.length();

        byte[] msgHash = Hash.sha3((prefix + message).getBytes());


        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);

        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        SignatureData sd = new SignatureData(
                v,
                Arrays.copyOfRange(signatureBytes, 0, 32),
                Arrays.copyOfRange(signatureBytes, 32, 64));

        String addressRecovered = null;
        boolean match = false;

        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            BigInteger publicKey = Sign.recoverFromSignature(
                    (byte) i,
                    new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
                    msgHash);

            if (publicKey != null) {
                addressRecovered = "0x" + Keys.getAddress(publicKey);
                System.out.println("addressRecovered: " + addressRecovered);
                if (addressRecovered.equals(address)) {
                    match = true;
                    break;
                }
            }
        }
        System.out.println("match: " + match);
        return match;
    }
}
