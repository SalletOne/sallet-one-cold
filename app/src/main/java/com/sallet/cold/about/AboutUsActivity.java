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
import butterknife.BindView;
import butterknife.OnClick;

/**

 * about us
 * Used to display information about this product
 */
public class AboutUsActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_serve)
    TextView tvServe;
    @BindView(R.id.tv_about_us)
    TextView tvAboutUs;
    @BindView(R.id.tv_call_us)
    TextView tvCallUs;
    @BindView(R.id.iv_submit)
    ImageView ivSubmit;
    @BindView(R.id.ll_check_sign)
    LinearLayout llCheckSign;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
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
