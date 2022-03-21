package com.sallet.cold.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PassUtil {

    public static String isStringPwd(String password) {
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < password.length(); i++) {
            int A = password.charAt(i);
            if (A >= 48 && A <= 57) {//
                map.put("数字", "数字");
                map.put("alot", "alot");
            } else if (A >= 65 && A <= 90) {//
                map.put("大写", "大写");
            } else if (A >= 97 && A <= 122) {//
                map.put("小写", "小写");
            }
//            else {
//                map.put("特殊", "特殊");
//            }
        }
        Set<String> sets = map.keySet();
        int pwdSize = sets.size();//
        int pwdLength = password.length();//
        if (pwdSize >= 3 && pwdLength >= 8) {
            return "1";//
        } else {
            return "0";//
        }
    }



    public static String isStringPwd10(String password) {
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < password.length(); i++) {
            int A = password.charAt(i);
            if (A >= 48 && A <= 57) {//
                map.put("数字", "数字");
                map.put("alot", "alot");
            } else if (A >= 65 && A <= 90) {//
                map.put("大写", "大写");
            } else if (A >= 97 && A <= 122) {//
                map.put("小写", "小写");
            }
//            else {
//                map.put("特殊", "特殊");
//            }
        }
        Set<String> sets = map.keySet();
        int pwdSize = sets.size();//
        int pwdLength = password.length();//
        if (pwdSize >= 3 && pwdLength >= 10) {
            return "1";//
        } else {
            return "0";//
        }
    }
}
