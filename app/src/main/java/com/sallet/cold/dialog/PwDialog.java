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
 * 钱包输入密码弹窗，输入正确的密码用来生成钱包地址和交易签名
 * Wallet input password pop-up window, enter the correct password to generate wallet address and transaction signature
 */
public class PwDialog extends BaseDialog {
    Activity context;//上下文 context
    String content;//输入框提示 input box prompt
    String receive="";//接收地址 receiving address
    OnPress onPress;//回调 callback

    public interface OnPress{
        void onPress();
    }

    /**
     * 构造方法
     * @param context    上下文
     * @param content   输入框提示内容
     * @param onPress   回调对象
     * @param receive   接收地址
     * Construction method
     * @param context context
     * @param content input box prompt content
     * @param onPress callback object
     * @param receive receiving address
     */
    public PwDialog(Activity context, String content, OnPress onPress,String ...receive) {
        super(context);
        //给私有对象赋值
        //Assign value to private object
        this.context=context;
        this.content=content;
        this.onPress=onPress;
        this.receive=receive[0];
        View convertView = getLayoutInflater().inflate(R.layout.dialog_input_pass, null);
        setContentView(convertView);
    }

    /**
     * 构造方法
     * @param context    上下文
     * @param content   输入框提示内容
     * @param onPress   回调对象
     * @param receive   接收地址
     * Construction method
     * @param context context
     * @param content input box prompt content
     * @param onPress callback object
     * @param receive receiving address
     */
    public PwDialog(Activity context,String sign,String content,OnPress onPress,String ...receive) {
        super(context);
        //给私有对象赋值
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
         * 绑定UI
         * Bind UI
         */
        final EditText et_pass=findViewById(R.id.et_pass);
        final ImageView iv_close=findViewById(R.id.iv_close);
        final TextView tv_content=findViewById(R.id.tv_content);
        final TextView tvReceive=findViewById(R.id.tv_receive);
        final TextView tvJieshou=findViewById(R.id.tv_jieshou);
        tv_content.setText(content);
        if(receive.length()>0) {
            //如果接收地址有值，则展示
            //If the receiving address has a value, display
            tvReceive.setVisibility(View.VISIBLE);
            tvJieshou.setVisibility(View.VISIBLE);
            tvReceive.setText(receive);
        }else {
            //如果无接收地址则不展示
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
                //校验密码是否正确
                //Verify that the password is correct
                if (PasswordUtils.validatePassword(et_pass.getText().toString(),App.getSpString(App.passWordDecode))) {
                    App.passWord = et_pass.getText().toString();
                    dismiss();
                    onPress.onPress();
                    //正确则通过回调
                    //Callback if correct
                }else {
                    //密码错误倒计时销毁钱包
                    //Password error countdown to destroy wallet
                    if(DoubleClickUtil.isDoubleClick(10*60*1000)) {
                        App.passWordTime--;
                        showToast(String.format(getString(R.string.password_wrong), App.passWordTime));
                        if (App.passWordTime == 0) {
                            //十分钟内错误次数超过5次销毁钱包清除所有数据
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
            //有密码提示则展示密码提示
            //If there is a password hint, show the password hint
            tvHint.setText(getString(R.string.password_submit) + App.getSpString(App.passSubmit));
        }else {
            //没有密码提示则隐藏控件
            //Hide controls without password prompt
            tvHint.setVisibility(View.GONE);
        }

    }
}
