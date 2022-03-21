package com.sallet.cold.utils;

import android.util.Log;

public class L {
    private static boolean validLog(String str) {
        return (str == null || str.length() == 0) ? false : false;
    }

    public static void d(String str, String str2) {
        if (validLog(str2)) {
            Log.d(str, str2);
        }
    }

    public static void d(String str, String str2, Throwable th) {
        if (validLog(str2)) {
            Log.d(str, str2, th);
        }
    }

    public static void i(String str, String str2) {
        if (validLog(str2)) {
            Log.i(str, str2);
        }
    }

    public static void i(String str, String str2, Throwable th) {
        if (validLog(str2)) {
            Log.i(str, str2, th);
        }
    }

    public static void w(String str, String str2) {
        if (validLog(str2)) {
            Log.w(str, str2);
        }
    }

    public static void w(String str, String str2, Throwable th) {
        if (validLog(str2)) {
            Log.w(str, str2, th);
        }
    }

    public static void e(String str, String str2) {
        if (validLog(str2)) {
            Log.e(str, str2);
        }
    }

    public static void e(String str, String str2, Throwable th) {
        if (validLog(str2)) {
            Log.e(str, str2, th);
        }
    }
}
