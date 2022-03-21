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
 * activity基类用于每个activity常用的方法封装
 * The activity base class is used to encapsulate the methods commonly used in each activity
 *
 */
public class BaseActivity extends AppCompatActivity {
    //外部调用 上下文
    //external call context
    public Activity context;
    //吐司
    //toast
    private static Toast toast;
    //加载状态dialog
    //Loading status dialog
    private Dialog mDialog ;

    private NetChangeReceiver netBroadcastReceiver;
    /**
     * 网络类型
     */
    private int netType;
    private IsNetDialog isNetDialog;//有网提示的弹窗
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //强制竖屏
        //Force vertical screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //上下文赋值
        //context assignment
        context = this;
        //activity管理器，每个新开的activity页面都加入到一个集合中，便于销毁的时候一起处理
        //Activity manager, each newly opened activity page is added to a collection, which is easy to deal with when it is destroyed
        ActivityCollector.addActivity(this);
        new WebView(this).destroy();
        isNetDialog=new IsNetDialog(context);
        checkNet();
    }

    /**
     * 外部调用
     * 展示吐司的方法
     * @param content 展示的内容
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
        //隐藏键盘
        //hide keyboard
        hideKeyboard();
        toast.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除该activity
        //remove the activity
        ActivityCollector.removeActivity(this);
    }


    /**
     * 隐藏键盘
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
     * 改变语言
     * @param language 语言的简称
     * Change language
     * @param language short name of language
     */

    public void changeLanguage(String language){
        //获取资源对象
        //get resource object
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        //获取配置对象
        //get configuration object
        Configuration config = resources.getConfiguration();
        //本地语言
        //local language
        Locale locale;

        locale=new Locale(language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);//
        } else {
            config.locale = locale;//
        }
        //设置好语言保存到app存储中
        //Set the language and save it to the app storage
        App.saveString(App.language,language);
        //更新资源
        //Update resources
        resources.updateConfiguration(config, metrics);
        //设置jar包语言
        //Set jar package language
        setJarLanguage();
    }


    /**
     * 获取string资源文件
     * @param resource  文件名
     * Get string resource file
     * @param resource filename
     * @return
     */
    public String  getStringResources(int resource){
        return context.getResources().getString(resource);
    }


    /**
     * 取消加载
     * Cancel loading
     */

    public void cancleLoading() {
        loadDialogUtils.closeDialog(mDialog);
    }

    /**
     * 展示加载
     * show loading
     */
    public void showLoading() {
        mDialog = loadDialogUtils.createLoadingDialog(context, getResources().getString(R.string.dialog_loading11));
    }


    /**
     * 设置jar包语言
     * Set jar package language
     */
    private void setJarLanguage(){
        Map<String,String  > map=new HashMap<>();
        //遍历语言列表把list转成map
        //Traverse the language list and convert the list to a map
        for(Locale locale:App.supportLang){
            map.put(locale.getLanguage(),locale.getCountry());
        }

        String country=map.get(App.getSpString(App.language));
        //设置jar包默认语言
        //Set the default language of the jar package
        LangUtils.defaultLocale=new Locale(App.getSpString(App.language),country);

    }

    //检查网络
    private void checkNet() {
        netBroadcastReceiver = new NetChangeReceiver();
        netBroadcastReceiver.setNetChangeListener(new NetChangeReceiver.NetChangeListener() {
            @Override
            public void onChangeListener(int status) {
                netType = status;
                if (isNetConnect()) {
                    //TODO 有网时操作

                    isNetDialog.show();
                }else{

                    //TODO 无网时的操作
                    if (isNetDialog.isShowing()){
                        isNetDialog.dismiss();
                    }
                }
            }
        });
        //Android 7.0以上需要动态注册
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            //注册广播接收
            registerReceiver(netBroadcastReceiver, filter);
        }
    }
    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
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
