package com.sallet.cold.about;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sallet.cold.App;
import com.sallet.cold.R;
import com.sallet.cold.about.ShowUsActivity;
import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.dialog.SubmitDialog;
import com.sallet.cold.utils.CheckSign;
import com.sallet.cold.utils.ConfigContent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 关于我们
 * 用来展示本产品相关信息
 * about us
 * Used to display information about this product
 */
public class AboutUsActivity extends BaseActivity {


    @InjectView(R.id.rl_back)
    RelativeLayout rlBack;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    @InjectView(R.id.tv_version)
    TextView tvVersion;
    @InjectView(R.id.tv_serve)
    TextView tvServe;
    @InjectView(R.id.tv_about_us)
    TextView tvAboutUs;
    @InjectView(R.id.tv_call_us)
    TextView tvCallUs;
    @InjectView(R.id.iv_submit)
    ImageView ivSubmit;
    @InjectView(R.id.ll_check_sign)
    LinearLayout llCheckSign;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.inject(this);
        //版本号
        tvVersion.setText("v "+ ConfigContent.deviceVersion);
        if(App.getSpString("checkSign")==null){
            llCheckSign.setVisibility(View.VISIBLE);
            App.saveString("checkSign","1");
        }else {
            llCheckSign.setVisibility(View.GONE);
        }
        tvCallUs.setText(CheckSign.getSign(context));
    }

    @OnClick({R.id.rl_back, R.id.tv_serve, R.id.tv_about_us, R.id.tv_call_us,R.id.iv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_serve:
                startActivity(new Intent(context, WebActivity.class));
                break;
            case R.id.tv_about_us:
                startActivity(new Intent(context,ShowUsActivity.class));
                break;
            case R.id.tv_call_us:
                startActivity(new Intent(context,CallUsActivity.class));
                break;
            case R.id.iv_submit:
                new SubmitDialog(context,getString(R.string.check_submit)).show();
        }
    }
}
