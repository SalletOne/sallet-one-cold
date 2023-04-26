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
import butterknife.BindView;
import butterknife.OnClick;

/**
 *Create wallet success page
 */
public class CreatOkActivity extends BaseActivity {

    /**
     */
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_ok);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_ok)
    public void onClick() {
        if(getIntent().getStringExtra("words")!=null) {
            //Verify that the mnemonic was saved successfully
            App.saveString(App.word, getIntent().getStringExtra("words"));
        }
        //start home page
        startActivity(new Intent(context, MainActivity.class)
                .putExtra(App.password,getIntent().getStringExtra( App.password))
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
