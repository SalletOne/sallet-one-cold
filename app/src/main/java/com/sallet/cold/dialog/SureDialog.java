package com.sallet.cold.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sallet.cold.R;
import com.sallet.cold.start.ResumeWordActivity;

/**
 * Mnemonic backup reconfirmation popup
 */
public class SureDialog extends Dialog {
    Context context;//
    String words;//

    /**
     * Construction method
     * @param context context
     * @param words mnemonic
     */
    public SureDialog(Context context,String words) {
        this(context, R.style.quick_option_dialog);
        this.context=context;
        this.words=words;
    }

    /**
     * Constructor with theme
     * @param context context
     * @param themeResId theme
     */
    public SureDialog(Context context, int themeResId) {
        super(context, themeResId);
        //get layout
        View convertView = getLayoutInflater().inflate(R.layout.dialog_make_sure, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set layout
        setContentView(convertView);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //Displays at the bottom of the layout by default
        getWindow().setGravity(Gravity.BOTTOM);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        //Set layout properties
        getWindow().setAttributes(p);
        //OK button click event
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make sure to start the next page and pass the mnemonic to the next page
                context.startActivity(new Intent(context, ResumeWordActivity.class).putExtra("words",words));

            }
        });
        //close button click event
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close
                dismiss();

            }
        });
    }
}
