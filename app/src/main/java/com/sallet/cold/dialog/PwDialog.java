package com.sallet.cold.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sallet.cold.App;
import com.sallet.cold.R;
import com.hk.offline.utils.PasswordUtils;
import com.sallet.cold.start.StartActivity;
import com.sallet.cold.utils.DoubleClickUtil;

/**
 * Wallet input password pop-up window, enter the correct password to generate wallet address and transaction signature
 */
public class PwDialog extends BaseDialog {
    Activity context;// context
    String content;// input box prompt
    String receive="";// receiving address
    OnPress onPress;// callback

    public interface OnPress{
        void onPress(String password);
    }

    /**
     * Construction method
     * @param context context
     * @param content input box prompt content
     * @param onPress callback object
     * @param receive receiving address
     */
    public PwDialog(Activity context, String content, OnPress onPress,String ...receive) {
        super(context);
        //Assign value to private object
        this.context=context;
        this.content=content;
        this.onPress=onPress;
        this.receive=receive[0];
        View convertView = getLayoutInflater().inflate(R.layout.dialog_input_pass, null);
        setContentView(convertView);
    }

    /**
     * Construction method
     * @param context context
     * @param content input box prompt content
     * @param onPress callback object
     * @param receive receiving address
     */
    public PwDialog(Activity context,String sign,String content,OnPress onPress,String ...receive) {
        super(context);
        //Assign value to private object
        this.context=context;
        this.content=content;
        this.onPress=onPress;
        if(receive.length>0) {
            this.receive = receive[0];
        }
        View convertView = getLayoutInflater().inflate(R.layout.dialog_input_pass, null);
        setContentView(convertView);
    }
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.BOTTOM);

        /**
         * Bind UI
         */
        final EditText et_pass=findViewById(R.id.et_pass);
        final ImageView iv_close=findViewById(R.id.iv_close);
        final TextView tv_content=findViewById(R.id.tv_content);
        final TextView tvReceive=findViewById(R.id.tv_receive);
        final TextView tvJieshou=findViewById(R.id.tv_jieshou);
        tv_content.setText(content);
        if(receive.length()>0) {
            //If the receiving address has a value, display
            tvReceive.setVisibility(View.VISIBLE);
            tvJieshou.setVisibility(View.VISIBLE);
            tvReceive.setText(receive);
        }else {
            //If there is no receiving address, it will not be displayed
            tvReceive.setVisibility(View.GONE);
            tvJieshou.setVisibility(View.GONE);
        }
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DoubleClickUtil.isDoubleClick(800)){
                    return;
                }


                if(et_pass.getText().toString().equals("")){
                    showToast(getString(R.string.creat_money_pass_ac1));
                    return;
                }
                //Verify that the password is correct
                if (PasswordUtils.validatePassword(et_pass.getText().toString(),App.getSpString(App.passWordDecode))) {
                    dismiss();
                    onPress.onPress(et_pass.getText().toString());
                    //Callback if correct
                }else {
                    //Password error countdown to destroy wallet
                    if(DoubleClickUtil.isDoubleClick(10*60*1000)) {
                        App.passWordTime--;
                        showToast(String.format(getString(R.string.password_wrong), App.passWordTime));
                        if (App.passWordTime == 0) {
                            //The number of errors exceeds 5 times within ten minutes, the wallet is destroyed and all data is cleared
                            App.clear();
                            context.startActivity(new Intent(context, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }else {
                        App.passWordTime=5;
                    }
                }

            }
        });
        TextView tvHint=findViewById(R.id.tv_hint);
        if(App.getSpString(App.passSubmit)!=null) {
            //If there is a password hint, show the password hint
            tvHint.setText(getString(R.string.password_submit) + App.getSpString(App.passSubmit));
        }else {
            //Hide controls without password prompt
            tvHint.setVisibility(View.GONE);
        }

    }
}
