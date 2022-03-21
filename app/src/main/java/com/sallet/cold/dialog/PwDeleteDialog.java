package com.sallet.cold.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hk.offline.utils.PasswordUtils;
import com.sallet.cold.App;
import com.sallet.cold.R;
import com.sallet.cold.start.StartActivity;

/**
 * 校验销毁钱包密码弹窗,输入正确的销毁密码销毁钱包
 * Verify the destruction wallet password pop-up window,
 * enter the correct destruction password to destroy the wallet
 */
public class PwDeleteDialog extends BaseDialog {
    Context context;
    //监听回调
    //monitor callback
    OnPress onPress;
    //点击监听
    //Click to listen
    public interface OnPress{
        void onPress();
    }

    /**
     * 构造方法
     * @param context 上下文
     * @param onPress   监听回调对象
     * Construction method
     * @param context context
     * @param onPress listener callback object
     */
    public PwDeleteDialog(Context context, OnPress onPress) {
        super(context);
        this.onPress=onPress;
        //设置布局
        //set layout
        View convertView = getLayoutInflater().inflate(R.layout.dialog_delete_pass, null);
        setContentView(convertView);
    }


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //设置dialog展示位置为页面底部
        //Set the dialog placement to the bottom of the page
        getWindow().setGravity(Gravity.BOTTOM);


        final EditText et_pass=findViewById(R.id.et_pass);
        final ImageView iv_close=findViewById(R.id.iv_close);
        //关闭监听
        //close monitor
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭
                //closure
                dismiss();
            }
        });
        //确定按钮监听
        //OK button monitor
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getSpString(App.deletePass).equals(et_pass.getText().toString())) {
                    //如果输入的销毁密码正确，弹窗消失，并回调给主页面
                    //If the entered destruction password is correct, the pop-up window will disappear and call back to the main page
                    dismiss();
                    onPress.onPress();
                }else {

                    //输入销毁密码错误，提示错误
                    //Entering the destruction password is wrong, prompting an error
                    showToast(getString(R.string.pass_wrong));

                }

            }
        });


    }
}
