package com.sallet.cold.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sallet.cold.App;
import com.sallet.cold.R;

import org.jetbrains.annotations.NotNull;


import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 主页设置的弹窗,包含的硬件钱包的相关配置
 * The pop-up window of the home page settings, including the relevant configuration of the hardware wallet
 */
public class MainDialog extends BaseDialog {

    /**
     * 绑定UI
     * Bind UI
     */
    @InjectView(R.id.ll_coin_manange)
    LinearLayout llCoinManange;
    @InjectView(R.id.tv_delete_pwd)
    TextView tvDeletePwd;
    @InjectView(R.id.ll_set_pwd)
    LinearLayout llSetPwd;
    @InjectView(R.id.iv_language_type)
    ImageView ivLanguageType;
    @InjectView(R.id.ll_change_language)
    LinearLayout llChangeLanguage;
    private int type;//0是设置了销毁密码，1是未设置   0 is the destruction password is set, 1 is not set


    OnMainClick onMainClick;
    //点击监听回调
    //click listener callback
    public interface OnMainClick{
        void onClick(int position,int type);
    }

    /**
     *  主页弹窗的构造方法
     * @param context 上下文
     * @param onMainClick 回调监听对象
     * The construction method of the home page pop-up window
     * @param context context
     * @param onMainClick callback listener object
     */
    public MainDialog(@NonNull @NotNull Context context,OnMainClick onMainClick)  {
        super(context);
        setContentView(R.layout.dialog_main);
        this.onMainClick=onMainClick;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置弹窗展示位置为顶部
        //Set popup placement to top
        getWindow().setGravity(Gravity.TOP);
        ButterKnife.inject(this);
        //判断是否设置了钱包的销毁密码
        //Determine whether the wallet's destruction password is set
        if(App.getSpString(App.deletePass)!=null){
            //如果设置了销毁密码直接展示销毁钱包
            //If the destruction password is set, directly display the destruction wallet
            tvDeletePwd.setText(getString(R.string.destruction_wallet));
            type=0;
        }else {
            //未设置销毁密码显示让用户去设置销毁密码
            //If the destruction password is not set, the user is asked to set the destruction password.
            tvDeletePwd.setText(getString(R.string.destruction_wallet_pass));
            type=1;
        }


    }

    @OnClick({R.id.ll_coin_manange, R.id.ll_set_pwd, R.id.ll_change_language,R.id.ll_about_us,R.id.ll_check,
    R.id.ll_backup_words})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_coin_manange:
                //进入代币管理页面
                //Go to the token management page
                onMainClick.onClick(0,1);
                dismiss();
                break;
            case R.id.ll_set_pwd:
                //进入设置销毁密码页面
                //Go to the Set Destruction Password page
                onMainClick.onClick(1,type);
                dismiss();
                break;
            case R.id.ll_change_language:
                //进入修改语言页面
                //Go to the Modify language page
                onMainClick.onClick(2,type);
                dismiss();
                break;
            case R.id.ll_about_us:
                //进入关于我们页面
                //Go to about us page
                onMainClick.onClick(3,type);
                dismiss();
                break;
            case R.id.ll_check:
                //进入检测更新页面
                //Go to the check update page
                onMainClick.onClick(4,type);
                dismiss();
                break;
            case R.id.ll_backup_words:
                //进入备份助记词页面
                //Go to the backup mnemonic page
                onMainClick.onClick(5,type);
                dismiss();
                break;
        }
    }
}
