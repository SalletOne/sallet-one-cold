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
 * Verify the destruction wallet password pop-up window,
 * enter the correct destruction password to destroy the wallet
 */
public class PwDeleteDialog extends BaseDialog {
    Context context;
    //monitor callback
    OnPress onPress;
    //Click to listen
    public interface OnPress{
        void onPress();
    }

    /**
     * Construction method
     * @param context context
     * @param onPress listener callback object
     */
    public PwDeleteDialog(Context context, OnPress onPress) {
        super(context);
        this.onPress=onPress;
        //set layout
        View convertView = getLayoutInflater().inflate(R.layout.dialog_delete_pass, null);
        setContentView(convertView);
    }


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //Set the dialog placement to the bottom of the page
        getWindow().setGravity(Gravity.BOTTOM);


        final EditText et_pass=findViewById(R.id.et_pass);
        final ImageView iv_close=findViewById(R.id.iv_close);
        //close monitor
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //closure
                dismiss();
            }
        });
        //OK button monitor
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getSpString(App.deletePass).equals(et_pass.getText().toString())) {
                    //If the entered destruction password is correct, the pop-up window will disappear and call back to the main page
                    dismiss();
                    onPress.onPress();
                }else {

                    //Entering the destruction password is wrong, prompting an error
                    showToast(getString(R.string.pass_wrong));

                }

            }
        });


    }
}
