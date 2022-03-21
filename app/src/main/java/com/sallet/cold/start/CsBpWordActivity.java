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
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 让用户选择是否需要备份助记词
 * prompt note mnemonic，Generate mnemonic
 */
public class CsBpWordActivity extends BaseActivity {

    /**
     * 绑定UI
     * Bind UI
     */
    @InjectView(R.id.bt1)
    TextView bt1;
    @InjectView(R.id.bt2)
    TextView bt2;
    private String[] value;//助记词

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sc_bp_word);
        ButterKnife.inject(this);


        //生成助记词返回一个集合
        //generate mnemonic
        List<String> list = new Mnemonic().words();
        value = new String[12];
        //把集合转为数组
        //Convert collection to array
        list.toArray(value);

    }

    @OnClick({R.id.bt1, R.id.bt2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                //前往备份助记词页面
                //Backup verification mnemonic
                startActivity(new Intent(context,BackUpWordActivity.class).putExtra("words",AesUtils.aesEncrypt(StringUtils.join(value, ","))));
                break;
            case R.id.bt2:
                //保存助记词到APP存储中
                //Save the mnemonic to the APP storage
                App.saveString(App.word, AesUtils.aesEncrypt(StringUtils.join(value, ",")));
                //稍后备份助记词直接进入主页
                //Go directly to the home page, not backed up
                startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                break;
        }
    }
}
