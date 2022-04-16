package com.sallet.cold.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.sallet.cold.App;

import java.util.UUID;

import static android.text.TextUtils.isEmpty;

public class ConfigContent {


    //和在线端交互的传输协议
    //The transport protocol that interacts with the online side
    public static final String agree="odfp://";
    //设备号
    //Device No
    public static  String deviceCode="";
    //设备版本
    //Device version
    public static  String deviceVersion="";


    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        deviceId.append("a");
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if(!isEmpty(wifiMac)){
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if(!isEmpty(imei)){
                deviceId.append("imei");
                deviceId.append(imei);
                Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
            String sn = tm.getSimSerialNumber();
            if(!isEmpty(sn)){
                deviceId.append("sn");
                deviceId.append(sn);
                Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
            String uuid = getUUID(context);
            if(!isEmpty(uuid)){
                deviceId.append("id");
                deviceId.append(uuid);
                Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID(context));
        }
        Log.e("getDeviceId : ", deviceId.toString());
        return deviceId.toString();
    }
    /**
     */
    public static String getUUID(Context context){
        String uuid;
        uuid = App.getSpString("uuid");
        if(isEmpty(uuid)){
            uuid = UUID.randomUUID().toString();
            App.saveString("uuid",uuid);
        }
        return uuid;
    }
}
