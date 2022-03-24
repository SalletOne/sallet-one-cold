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
 * Hardware Startup Page
 */

public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Change language to saved language
        changeLanguage(App.getSpString(App.language));
        setContentView(R.layout.activity_splash);

        //Get user storage in APP
        App.sp = getSharedPreferences("USER", Context.MODE_PRIVATE);

        //Verify that you have logged in, and you have logged in directly to the homepage

        if (App.getSpString(App.isLogin) != null && App.getSpString(App.isLogin).equals("true")) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }else {
            startActivity(new Intent(SplashActivity.this, StartActivity.class));
        }



    }
}
