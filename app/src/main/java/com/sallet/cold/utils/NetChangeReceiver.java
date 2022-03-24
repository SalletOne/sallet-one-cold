package com.sallet.cold.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetChangeReceiver extends BroadcastReceiver {
    public NetChangeListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            if (listener != null) {
                listener.onChangeListener(netWorkState);
            }
        }
    }

    public interface NetChangeListener {
        void onChangeListener(int status);
    }

    public void setNetChangeListener(NetChangeListener changeListener) {
        this.listener = changeListener;
    }
}
