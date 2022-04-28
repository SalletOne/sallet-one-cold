package com.sallet.cold.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sallet.cold.App;
import com.sallet.cold.MainActivity;
import com.sallet.cold.R;
import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.utils.AesUtils;
import com.hk.offline.utils.Mnemonic;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * prompt note mnemonic，Generate mnemonic
 */
public class CsBpWordActivity extends BaseActivity {

    /**
     * Bind UI
     */
    @BindView(R.id.bt1)
    TextView bt1;
    @BindView(R.id.bt2)
    TextView bt2;
    private String[] value;//

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sc_bp_word);
        ButterKnife.bind(this);


        //generate mnemonic
        List<String> list = new Mnemonic().words();
        value = new String[12];
        //Convert collection to array
        list.toArray(value);

    }

    @OnClick({R.id.bt1, R.id.bt2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                //Backup verification mnemonic
                startActivity(new Intent(context,BackUpWordActivity.class)
                        .putExtra( App.password,getIntent().getStringExtra(App.password))
                        .putExtra("words",AesUtils.aesEncrypt(getIntent().getStringExtra( App.password),StringUtils.join(value, ","))));
                break;
            case R.id.bt2:
                //Save the mnemonic to the APP storage
                App.saveString(App.word, AesUtils.aesEncrypt(getIntent().getStringExtra( App.password),StringUtils.join(value, ",")));
                //Go directly to the home page, not backed up
                startActivity(new Intent(context, MainActivity.class)
                        .putExtra(App.password,getIntent().getStringExtra( App.password))
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                break;

        }
    }
}
