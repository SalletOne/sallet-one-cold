package com.sallet.cold.utils;

import org.bitcoinj.crypto.PBKDF2SHA512;
import org.web3j.utils.Numeric;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class KDFUtils {
    //迭代次数
    private static final int PBKDF2_ROUNDS = 2048;






    /**
     * KDF函数(用户输入的密码单向唯一加密)
     * @param password 用户设置的密码
     * @return
     */
    public static byte[] aesKeyGenerate(String password,String salt){
        byte[] aesKey = PBKDF2SHA512.derive(password, salt, PBKDF2_ROUNDS, 32);
        //转换成十六进制
        return aesKey;
    }


    public static String  generateRandomBytes(int size) {
        byte[] bytes = new byte[size];
        new SecureRandom().nextBytes(bytes);
        return Numeric.toHexStringNoPrefix(bytes);
    }



}
