package com.sallet.cold.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * Network Status Monitoring Popup
 */

public class NetChangeReceiver extends BroadcastReceiver {
    public NetChangeListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // Network Status Monitoring Popup
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // When the network changes, determine the current network status and call back the current network status through NetEvent
            if (listener != null) {
                listener.onChangeListener(netWorkState);
            }
        }
    }

    // custom interface
    public interface NetChangeListener {
        void onChangeListener(int status);
    }

    public void setNetChangeListener(NetChangeListener changeListener) {
        this.listener = changeListener;
    }
}
