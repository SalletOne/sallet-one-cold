package com.sallet.cold.utils;

import android.util.Base64;

import com.sallet.cold.App;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

    public class AesUtils {
        public static final String bm = "UTF-8";

        /**
         * Obtain Random Numbers
         * @param length
         * @return
         */
        public static String getRandom(int length){
            char[] arr = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k',
                    'l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
            StringBuilder result = new StringBuilder(String.valueOf(arr[(int) Math.floor(Math.random() * 36)]));
            for(int i = 1;i<length;i++){
                result.append(arr[(int) Math.floor(Math.random() * 36)]);
            }
            return result.toString();
        }






        /**AES encryption
         *
         * @param
         * @return
         */

        public static String aesEncrypt(String password,String content) {
            String salt=KDFUtils.generateRandomBytes(32);
            App.saveString("salt",salt);
            String iv=getRandom(16);
            byte[] passBytes=KDFUtils.aesKeyGenerate(password.substring(0,8)+"12345678",salt);
            SecretKeySpec key = new SecretKeySpec(passBytes, "AES");
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes());
                App.saveString("iv",iv);

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
         *AES decryption
         * @return
         */

        public static String aesDecrypt(String password,String content) {

            String salt=App.getSpString("salt");
            String iv=App.getSpString("iv");
            byte[] passBytes=KDFUtils.aesKeyGenerate(password.substring(0,8)+"12345678",salt);
            try {
                byte[] byteMi = Base64.decode(content,Base64.DEFAULT);
                IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes());
                SecretKeySpec key = new SecretKeySpec(passBytes, "AES");
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
