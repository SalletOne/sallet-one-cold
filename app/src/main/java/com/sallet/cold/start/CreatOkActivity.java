package com.sallet.cold.start;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sallet.cold.App;
import com.sallet.cold.MainActivity;
import com.sallet.cold.R;
import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.utils.AesUtils;

import org.apache.commons.lang3.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 创建钱包成功页面，点击OK进入钱包主页
 */
public class CreatOkActivity extends BaseActivity {

    /**
     * 绑定UI
     */
    @InjectView(R.id.tv_ok)
    TextView tvOk;
    @InjectView(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_ok);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.tv_ok)
    public void onClick() {
        if(getIntent().getStringExtra("words")!=null) {
            //把生成的助记词加密保存在App存储中
            //Verify that the mnemonic was saved successfully
            App.saveString(App.word, getIntent().getStringExtra("words"));
        }
        //启动主页
        //start home page
        startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
