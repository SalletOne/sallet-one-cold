package com.sallet.cold.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
    /**
     * No network
     */
    private static final int NETWORK_NONE = -1;
    /**
     * Mobile network
     */
    private static final int NETWORK_MOBILE = 0;
    /**
     * wireless network
     */
    private static final int NETWORK_WIFI = 1;

    public static int getNetWorkState(Context context) {
        //Get Connection Manager Object
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        //If the network is connected, determine the type of network
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;//wifi
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;//mobile
            }
        } else {
            //network anomaly
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }
}
