package com.sallet.cold.start;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.sallet.cold.App;
import com.sallet.cold.R;
import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.utils.AesUtils;
import com.sallet.cold.utils.PassUtil;
import com.hk.offline.utils.PasswordUtils;

import org.apache.commons.lang3.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 设置钱包密码,该密码用于解析加密的助记词，生成地址和交易签名都需要校验该密码
 * Set the wallet password. This password is used to parse the encrypted mnemonic.
 * It is necessary to verify the password when generating the address and transaction signature.
 */
public class CreatMoneyPassActivity extends BaseActivity {


    /**
     * 绑定UI
     * Bind UI
     */
    @InjectView(R.id.rl_back)
    RelativeLayout rlBack;
    @InjectView(R.id.et_pass)
    EditText etPass;
    @InjectView(R.id.et_pass2)
    EditText etPass2;
    @InjectView(R.id.et_pass_submit)
    EditText etPassSubmit;
    @InjectView(R.id.bt)
    TextView bt;
    @InjectView(R.id.tv_hint)
    TextView tv_hint;
    @InjectView(R.id.iv_is_see)
    ImageView ivSee;
    private boolean isSee = false;//密码是否可见 Is the password visible?

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_pass);
        ButterKnife.inject(this);
        //监听密码输入框
        //Monitor password input box
        etPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //输入框有值设置按钮状态
                //The input box has a value to set the button state
                bt.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @OnClick({R.id.rl_back, R.id.bt, R.id.iv_is_see})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.iv_is_see:
                if (isSee) {
                    //设置密码可见状态时的UI
                    //UI when setting password visible state
                    etPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivSee.setImageResource(R.mipmap.ic_see);
                    isSee = false;

                } else {
                    //设置密码不可见状态时的UI
                    //UI when setting the password invisible state
                    etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPass2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivSee.setImageResource(R.mipmap.ic_nosee);
                    isSee = true;
                }
                //设置光标位置
                //set cursor position
                etPass.setSelection(etPass.getText().toString().length());//
                break;
            case R.id.bt:
                //首次输入密码无值返回错误提示
                //Entering the password for the first time returns an error message with no value
                if (etPass.getText().toString().length() <= 0) {
                    showToast(getStringResources(R.string.creat_money_pass_ac5));
                    return;
                }

                if (PassUtil.isStringPwd(etPass.getText().toString()).equals("0")) {
                    //校验密码格式不对提示
                    //Check password format is incorrect
                    tv_hint.setVisibility(View.VISIBLE);
                    tv_hint.setText(getStringResources(R.string.creat_money_pass_ac1));
                    return;
                } else {

                    tv_hint.setVisibility(View.GONE);

                }
                //重复输入密码无值返回错误提示
                //Repeatedly enter the password without value and return an error prompt
                if (etPass2.getText().toString().length() <= 0) {
                    showToast(getStringResources(R.string.creat_money_pass_ac4));
                    return;
                }
                //比较两次密码是否一致
                //Compare the two passwords for the same
                if (etPass.getText().toString().equals(etPass2.getText().toString())) {
                    //Save wallet password
                    //保存钱包密码
                    App.passWord = etPass.getText().toString();
                    App.saveString(App.passWordDecode, PasswordUtils.encryptPassword(etPass.getText().toString()));
                    //password hint
                    //保存密码提示
                    if (etPassSubmit.getText().toString().length() > 0) {
                        App.saveString(App.passSubmit, etPassSubmit.getText().toString());
                    }
                    if (getIntent().getIntExtra("type", 0) == 1) {
                        //type是1，是恢复钱包操作，直接进入钱包主页
                        //To restore wallet, go directly to
                        App.saveString(App.word, AesUtils.aesEncrypt(StringUtils.join(getIntent().getStringArrayExtra("wordList"), ",")));
                        startActivity(new Intent(CreatMoneyPassActivity.this, CreatOkActivity.class));

                    } else {
                        //其他情况进入提示用户是否需要备份助记词页面
                        //Create a wallet, prompt to verify the mnemonic
                        startActivity(new Intent(CreatMoneyPassActivity.this, CsBpWordActivity.class));
                    }
                    tv_hint.setVisibility(View.GONE);
                } else {
                    tv_hint.setVisibility(View.VISIBLE);
                    tv_hint.setText(getStringResources(R.string.creat_money_pass_ac3));
                }

                break;
        }
    }
}
