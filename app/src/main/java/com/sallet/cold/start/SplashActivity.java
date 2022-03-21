package com.sallet.cold.start;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.sallet.cold.App;
import com.sallet.cold.MainActivity;
import com.sallet.cold.R;
import com.sallet.cold.base.BaseActivity;

/**
 * 硬件启动页
 * Hardware Startup Page
 */

public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //更改语言为保存的语言
        //Change language to saved language
        changeLanguage(App.getSpString(App.language));
        setContentView(R.layout.activity_splash);

        //获取APP中的user存储
        //Get user storage in APP
        App.sp = getSharedPreferences("USER", Context.MODE_PRIVATE);

        //校验是否登录过，登录过直接进主页
        //Verify that you have logged in, and you have logged in directly to the homepage

        if (App.getSpString(App.isLogin) != null && App.getSpString(App.isLogin).equals("true")) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }else {
            startActivity(new Intent(SplashActivity.this, StartActivity.class));
        }



    }
}
