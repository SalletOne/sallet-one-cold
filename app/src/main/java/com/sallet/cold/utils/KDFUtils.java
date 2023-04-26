package com.sallet.cold.utils;

import org.bitcoinj.crypto.PBKDF2SHA512;
import org.web3j.utils.Numeric;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class KDFUtils {
    //iterations
    private static final int PBKDF2_ROUNDS = 2048;






    /**
     *KDF function (one-way unique encryption of user input password)
     *@ param password The password set by the user
     * @return
     */
    public static byte[] aesKeyGenerate(String password,String salt){
        byte[] aesKey = PBKDF2SHA512.derive(password, salt, PBKDF2_ROUNDS, 32);
        //Convert to hexadecimal
        return aesKey;
    }


    public static String  generateRandomBytes(int size) {
        byte[] bytes = new byte[size];
        new SecureRandom().nextBytes(bytes);
        return Numeric.toHexStringNoPrefix(bytes);
    }



}
