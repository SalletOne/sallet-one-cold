package com.sallet.cold.view;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.sallet.cold.utils.js.JsWrapperHelper;

import java.util.ArrayList;

public class AdaJsHelper extends JsWrapperHelper {


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public AdaJsHelper(Context context, String str,String str2,final Callback callback) {
        super(context, str, str2, callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void  getAddress(String str, Callback callback){
        executeJS("getAdaAddress('" + str + "')", callback);
    };


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getSign(String str, String str2, String str3, long j, String str4, JsWrapperHelper.Callback callback) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        arrayList.add(str2);
        arrayList.add(str3);
        arrayList.add(Long.valueOf(j));
        arrayList.add(str4);
        executeJS("sendTx", arrayList, callback);
    }
}
