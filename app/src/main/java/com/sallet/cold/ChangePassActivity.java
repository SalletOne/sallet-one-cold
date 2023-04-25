package com.sallet.cold;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.utils.PassUtil;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;


/**
 *  Set the destruction password,
 *  the destruction password is used to destroy the wallet and restore it to an unused state,
 *  clearing all user data
 */
public class ChangePassActivity extends BaseActivity {

    /**
     * Bind UI
     */
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_titel)
    TextView tvTitel;
    @BindView(R.id.et_pass)
    EditText etPass;
    @BindView(R.id.et_pass2)
    EditText etPass2;
    @BindView(R.id.bt)
    TextView bt;
    @BindView(R.id.iv_is_see)
    ImageView ivSee;

    private boolean isSee = false;// Is the password visible?

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        //Bind UI
        ButterKnife.bind(this);
        ivSee.setOnClickListener(v->{
                    if (isSee) {
                        //password is visible
                        etPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        ivSee.setImageResource(R.mipmap.ic_see);
                        isSee = false;

                    } else {
                        //Password is not visible
                        etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        ivSee.setImageResource(R.mipmap.ic_nosee);
                        isSee = true;
                    }
                    //set cursor position
                    etPass.setSelection(etPass.getText().toString().length());//
                }
                );
    }

    /**
     * control click event
     * @param view
     */
    @OnClick({R.id.rl_back, R.id.bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                //end page
                finish();
                break;
            case R.id.bt:
                //Submission steps
                submit();
                break;
        }
    }


    private void submit(){
        //Verify that the password format is correct
        if(PassUtil.isStringPwd10(etPass.getText().toString()).equals("0")){
            showToast(getStringResources(R.string.no_lenth_pass));
            return;
        }
        //Verify that the two passwords are the same
        if(!etPass.getText().toString().equals(etPass2.getText().toString())){
            showToast(getStringResources(R.string.pass_not_eq));
        }else {
            //If all verifications pass, save the password
            showToast(getStringResources(R.string.setting_success));
            App.saveString(App.deletePass,etPass.getText().toString());
            finish();
        }


    }
}
