package com.sallet.cold.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import org.bouncycastle.util.encoders.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class CheckSign {
    public static String getSign(Context ctx) {
        try {
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            MessageDigest md1 = MessageDigest.getInstance("MD5");
            md1.update(sign.toByteArray());
            byte[] digest = md1.digest();
            String res = toHexString(digest);
            MessageDigest md2 = MessageDigest.getInstance("SHA1");
            md2.update(sign.toByteArray());
            byte[] digest2 = md2.digest();
            String res2 = toHexString(digest2);
            String apkhash=apkVerifyWithSHA(ctx);
            return "SHA1: "+res2+"\n"+"MD5: "+res+"\n"+"APK hash: "+apkhash;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String  apkVerifyWithSHA(Context context) {
        String apkPath = context.getPackageCodePath(); // 获取Apk包存储路径
        try {
            MessageDigest dexDigest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = new byte[1024];
            int byteCount;
            FileInputStream fis = new FileInputStream(new File(apkPath)); // 读取apk文件
            while ((byteCount = fis.read(bytes)) != -1) {
                dexDigest.update(bytes, 0, byteCount);
            }
            BigInteger bigInteger = new BigInteger(1, dexDigest.digest()); // 计算apk文件的哈希值
            String sha = bigInteger.toString(16);
            return sha;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String toHexString(byte[] data) {
        return data == null ? "" : Hex.toHexString(data);
    }
}
