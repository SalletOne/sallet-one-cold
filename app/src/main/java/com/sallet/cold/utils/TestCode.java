package com.sallet.cold.utils;

import android.util.Log;

import org.bouncycastle.crypto.generators.SCrypt;
import org.tron.trident.utils.Numeric;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletUtils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import cn.hutool.core.util.HexUtil;

public class TestCode {

    public static void main() throws CipherException {
        // 使用key store方式加密助记词

        String s = "butter bracket hold road damp disease cube hope cream chair dune exhibit";

        String hex = HexUtil.encodeHexStr(s.getBytes(StandardCharsets.UTF_8));
        System.out.println(hex);
        System.out.println(new String(HexUtil.decodeHex(hex)));

        System.out.println("-------------");

        String ts = org.bouncycastle.util.encoders.Hex.toHexString(s.getBytes(StandardCharsets.UTF_8));
        System.out.println("hex 固定值: "+ ts);
        byte[] bbb = org.bouncycastle.util.encoders.Hex.decode(ts);
        System.out.println(Arrays.toString(bbb));
        System.out.println(new String(bbb));

//        WalletFile walletFile = new WalletFile();
//        Wallet.createLight(passphrase, keyPair);


        Long t1 = System.currentTimeMillis()/1000;
        System.out.println();
        try {
            yy();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        Log.d("time",(System.currentTimeMillis()/1000 - t1)+"");
    }

    public static void yy() throws NoSuchAlgorithmException, InvalidKeySpecException {

        String password = "123456";
        PBKDF2Util pbkdf2Util = new PBKDF2Util();

        String salt = pbkdf2Util.generateSalt();
        String pbkdf2 = pbkdf2Util.getEncryptedPassword(password,salt);

        System.out.println("原始密码:"+password);
        System.out.println("盐值: "+salt);
        System.out.println("PBKDF2加盐后的密码: "+pbkdf2);
        System.out.println("Test success");


    }
    public static void ts(byte[] bbb) throws CipherException {
        String password = "1234qwer";
        byte[] salt = generateRandomBytes(32);

        byte[] derivedKey = SCrypt.generate(
                password.getBytes(StandardCharsets.UTF_8),
                salt,
                1 << 18,
                8,
                6,
                32);

        byte[] encryptKey = Arrays.copyOfRange(derivedKey, 0, 16);

        byte[] iv = generateRandomBytes(16);

        byte[] cipherText = performCipherOperation(Cipher.ENCRYPT_MODE, iv, encryptKey, bbb);

        byte[] mac = generateMac(derivedKey, cipherText);

        /*System.out.println(Arrays.toString(derivedKey));
        System.out.println(Arrays.toString(cipherText));
        System.out.println(Arrays.toString(mac));*/
        System.out.println(Numeric.toHexStringNoPrefix(cipherText));
        System.out.println(Numeric.toHexStringNoPrefix(mac));

//        WalletUtils.generateWalletFile();

    }


    static byte[] generateRandomBytes(int size) {
        byte[] bytes = new byte[size];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

    private static byte[] performCipherOperation(
            int mode, byte[] iv, byte[] encryptKey, byte[] text) throws CipherException {

        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

            SecretKeySpec secretKeySpec = new SecretKeySpec(encryptKey, "AES");
            cipher.init(mode, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(text);
        } catch (NoSuchPaddingException
                | NoSuchAlgorithmException
                | InvalidAlgorithmParameterException
                | InvalidKeyException
                | BadPaddingException
                | IllegalBlockSizeException e) {
            throw new CipherException("Error performing cipher operation", e);
        }
    }

    private static byte[] generateMac(byte[] derivedKey, byte[] cipherText) {
        byte[] result = new byte[16 + cipherText.length];

        System.arraycopy(derivedKey, 16, result, 0, 16);
        System.arraycopy(cipherText, 0, result, 16, cipherText.length);

        return Hash.sha3(result);
    }
}
