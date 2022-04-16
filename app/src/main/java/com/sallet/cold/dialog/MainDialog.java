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
import butterknife.BindView;
import butterknife.OnClick;

/**
 * The pop-up window of the home page settings, including the relevant configuration of the hardware wallet
 */
public class MainDialog extends BaseDialog {

    /**
     * Bind UI
     */
    @BindView(R.id.ll_coin_manange)
    LinearLayout llCoinManange;
    @BindView(R.id.tv_delete_pwd)
    TextView tvDeletePwd;
    @BindView(R.id.ll_set_pwd)
    LinearLayout llSetPwd;
    @BindView(R.id.iv_language_type)
    ImageView ivLanguageType;
    @BindView(R.id.ll_change_language)
    LinearLayout llChangeLanguage;
    private int type;//  0 is the destruction password is set, 1 is not set


    OnMainClick onMainClick;
    //click listener callback
    public interface OnMainClick{
        void onClick(int position,int type);
    }

    /**
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
        //Set popup placement to top
        getWindow().setGravity(Gravity.TOP);
        ButterKnife.bind(this);
        //Determine whether the wallet's destruction password is set
        if(App.getSpString(App.deletePass)!=null){
            //If the destruction password is set, directly display the destruction wallet
            tvDeletePwd.setText(getString(R.string.destruction_wallet));
            type=0;
        }else {
            //If the destruction password is not set, the user is asked to set the destruction password.
            tvDeletePwd.setText(getString(R.string.destruction_wallet_pass));
            type=1;
        }


    }

    @OnClick({R.id.ll_coin_manange, R.id.ll_set_pwd, R.id.ll_change_language,R.id.ll_about_us,R.id.ll_check,
    R.id.ll_backup_words,R.id.ll_parent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_coin_manange:
                //Go to the token management page
                onMainClick.onClick(0,1);
                dismiss();
                break;
            case R.id.ll_set_pwd:
                //Go to the Set Destruction Password page
                onMainClick.onClick(1,type);
                dismiss();
                break;
            case R.id.ll_change_language:
                //Go to the Modify language page
                onMainClick.onClick(2,type);
                dismiss();
                break;
            case R.id.ll_about_us:
                //Go to about us page
                onMainClick.onClick(3,type);
                dismiss();
                break;
            case R.id.ll_check:
                //Go to the check update page
                onMainClick.onClick(4,type);
                dismiss();
                break;
            case R.id.ll_backup_words:
                //Go to the backup mnemonic page
                onMainClick.onClick(5,type);
                dismiss();
                break;
            case R.id.ll_parent:
                dismiss();
                break;
        }
    }
}
