package com.sallet.cold.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hk.offline.utils.LangUtils;
import com.sallet.cold.App;
import com.sallet.cold.R;
import com.sallet.cold.dialog.IsNetDialog;
import com.sallet.cold.utils.ActivityCollector;
import com.sallet.cold.utils.NetChangeReceiver;
import com.sallet.cold.utils.loadDialogUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The activity base class is used to encapsulate the methods commonly used in each activity
 *
 */
public class BaseActivity extends AppCompatActivity {
    //external call context
    public Activity context;
    //toast
    private static Toast toast;
    //Loading status dialog
    private Dialog mDialog ;

    private NetChangeReceiver netBroadcastReceiver;
    /**
     */
    private int netType;
    private IsNetDialog isNetDialog;//
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Force vertical screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //context assignment
        context = this;
        //Activity manager, each newly opened activity page is added to a collection, which is easy to deal with when it is destroyed
        ActivityCollector.addActivity(this);
        new WebView(this).destroy();
        isNetDialog=new IsNetDialog(context);
        checkNet();
    }

    /**

     * External call
     * How to show toast
     * @param content the content displayed
     */
    public void showToast(String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_LONG);

        } else {
            toast.setText(content);
        }
        //hide keyboard
        hideKeyboard();
        toast.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //remove the activity
        ActivityCollector.removeActivity(this);
    }


    /**
     * hide keyboard
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && this.getCurrentFocus() != null) {
            if (this.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    /**
     * Change language
     * @param language short name of language
     */

    public void changeLanguage(String language){
        //get resource object
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        //get configuration object
        Configuration config = resources.getConfiguration();
        //local language
        Locale locale;

        locale=new Locale(language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);//
        } else {
            config.locale = locale;//
        }
        //Set the language and save it to the app storage
        App.saveString(App.language,language);
        //Update resources
        resources.updateConfiguration(config, metrics);
        //Set jar package language
        setJarLanguage();
    }


    /**
     * Get string resource file
     * @param resource filename
     * @return
     */
    public String  getStringResources(int resource){
        return context.getResources().getString(resource);
    }


    /**
     * Cancel loading
     */

    public void cancleLoading() {
        loadDialogUtils.closeDialog(mDialog);
    }

    /**
     * show loading
     */
    public void showLoading() {
        mDialog = loadDialogUtils.createLoadingDialog(context, getResources().getString(R.string.dialog_loading11));
    }


    /**
     * Set jar package language
     */
    private void setJarLanguage(){
        Map<String,String  > map=new HashMap<>();
        //Traverse the language list and convert the list to a map
        for(Locale locale:App.supportLang){
            map.put(locale.getLanguage(),locale.getCountry());
        }

        String country=map.get(App.getSpString(App.language));
        //Set the default language of the jar package
        LangUtils.defaultLocale=new Locale(App.getSpString(App.language),country);

    }

    private void checkNet() {
        netBroadcastReceiver = new NetChangeReceiver();
        netBroadcastReceiver.setNetChangeListener(new NetChangeReceiver.NetChangeListener() {
            @Override
            public void onChangeListener(int status) {
                netType = status;
                if (isNetConnect()) {

                    isNetDialog.show();
                }else{

                    if (isNetDialog.isShowing()){
                        isNetDialog.dismiss();
                    }
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(netBroadcastReceiver, filter);
        }
    }
    /**
     *
     */
    public boolean isNetConnect() {
        if (netType == 1) {
            return true;
        } else if (netType == 0) {
            return true;
        } else if (netType == -1) {
            return false;
        }
        return false;
    }
}
