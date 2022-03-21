package com.sallet.cold.utils;

import android.util.Base64;

import com.sallet.cold.App;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

    public class AesUtils {
        public static final String VIPARA = "1234567890123456";
        public static final String bm = "UTF-8";
        public static final String password = App.passWord.substring(0,8)+"12345678";//password



        /**
         *
         * @param
         * @return
         */

        public static String aesEncrypt(String content) {
            try {
                IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
                SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
                byte[] encryptedData = cipher.doFinal(content.getBytes(bm));
                // return new String(encryptedData,bm);
                return new String(Base64.encode(encryptedData,Base64.DEFAULT));
//          return byte2HexStr(encryptedData);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         *
         * @return
         */

        public static String aesDecrypt(String content) {
            try {
                byte[] byteMi = Base64.decode(content,Base64.DEFAULT);
//          byte[] byteMi=  str2ByteArray(content);
                IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
                SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
                byte[] decryptedData = cipher.doFinal(byteMi);
                return new String(decryptedData, StandardCharsets.UTF_8);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
