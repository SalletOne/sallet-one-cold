package com.sallet.cold.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hk.offline.utils.LangUtils;
import com.sallet.cold.App;
import com.sallet.cold.MainActivity;
import com.sallet.cold.R;
import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.check.ResumeBallActivity;
import com.sallet.cold.utils.LanguageActivity;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Hardware start page for users to choose whether to create a wallet or restore an existing wallet

 */
public class StartActivity extends BaseActivity {

    /**
     * Bind UI
     */
    @InjectView(R.id.tv_1)
    TextView tv_1;
    @InjectView(R.id.tv_2)
    TextView tv_2;
    @InjectView(R.id.tv_3)
    TextView tv_3;
    @InjectView(R.id.tv_4)
    TextView tv_4;
    @InjectView(R.id.tv_5)
    TextView tv_5;
    @InjectView(R.id.tv_6)
    TextView tv_6;
    @InjectView(R.id.tv_change)
    TextView tvChange;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change language to saved language
        changeLanguage(App.getSpString(App.language));
        setContentView(R.layout.activity_start);
        ButterKnife.inject(this);

        //Get user storage in APP
        App.sp = getSharedPreferences("USER", Context.MODE_PRIVATE);

        // Create new wallet
        findViewById(R.id.ll_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, CreatMoneyPassActivity.class));

            }
        });
        // restore existing wallet
        findViewById(R.id.ll_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, ResumeBallActivity.class));

            }
        });
        // change language
        findViewById(R.id.ll_change_language).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, LanguageActivity.class));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        changeText();
    }

    /**
     * Modify UI language
     */
    private void changeText(){
        tv_1.setText(getStringResources(R.string.activity_start1));
        tvChange.setText(getStringResources(R.string.change_language));
        tv_2.setText(getStringResources(R.string.activity_start2));
        tv_3.setText(getStringResources(R.string.activity_start3));
        tv_4.setText(getStringResources(R.string.activity_start4));
        tv_5.setText(getStringResources(R.string.activity_start5));
        tv_6.setText(getStringResources(R.string.activity_start6));
    }
}
