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
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Set the wallet password. This password is used to parse the encrypted mnemonic.
 * It is necessary to verify the password when generating the address and transaction signature.
 */
public class CreatMoneyPassActivity extends BaseActivity {


    /**
     * Bind UI
     */
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.et_pass)
    EditText etPass;
    @BindView(R.id.et_pass2)
    EditText etPass2;
    @BindView(R.id.et_pass_submit)
    EditText etPassSubmit;
    @BindView(R.id.bt)
    TextView bt;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.iv_is_see)
    ImageView ivSee;
    private boolean isSee = false;// Is the password visible?

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_pass);
        ButterKnife.bind(this);
        //Monitor password input box
        etPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                    //UI when setting password visible state
                    etPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivSee.setImageResource(R.mipmap.ic_see);
                    isSee = false;

                } else {
                    //UI when setting the password invisible state
                    etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPass2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivSee.setImageResource(R.mipmap.ic_nosee);
                    isSee = true;
                }
                //set cursor position
                etPass.setSelection(etPass.getText().toString().length());//
                break;
            case R.id.bt:
                //Entering the password for the first time returns an error message with no value
                if (etPass.getText().toString().length() <= 0) {
                    showToast(getStringResources(R.string.creat_money_pass_ac5));
                    return;
                }

                if (PassUtil.isStringPwd(etPass.getText().toString()).equals("0")) {
                    //Check password format is incorrect
                    tv_hint.setVisibility(View.VISIBLE);
                    tv_hint.setText(getStringResources(R.string.creat_money_pass_ac1));
                    return;
                } else {

                    tv_hint.setVisibility(View.GONE);

                }
                //Repeatedly enter the password without value and return an error prompt
                if (etPass2.getText().toString().length() <= 0) {
                    showToast(getStringResources(R.string.creat_money_pass_ac4));
                    return;
                }
                //Compare the two passwords for the same
                if (etPass.getText().toString().equals(etPass2.getText().toString())) {
                    //Save wallet password
                    App.saveString(App.passWordDecode, PasswordUtils.encryptPassword(etPass.getText().toString()));
                    //password hint
                    if (etPassSubmit.getText().toString().length() > 0) {
                        App.saveString(App.passSubmit, etPassSubmit.getText().toString());
                    }
                    if (getIntent().getIntExtra("type", 0) == 1) {
                        //To restore wallet, go directly to
                        App.saveString(App.word, AesUtils.aesEncrypt(etPass.getText().toString(),StringUtils.join(getIntent().getStringArrayExtra("wordList"), ",")));
                        startActivity(new Intent(CreatMoneyPassActivity.this, CreatOkActivity.class)
                                .putExtra(App.password,etPass.getText().toString()));

                    } else {
                        //Create a wallet, prompt to verify the mnemonic
                        startActivity(new Intent(CreatMoneyPassActivity.this, CsBpWordActivity.class)
                        .putExtra( App.password,etPass.getText().toString()));
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
