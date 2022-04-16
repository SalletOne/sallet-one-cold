package com.sallet.cold.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.sallet.cold.R;

public class BaseDialog extends Dialog {
    //context
    Context context;

    /**
     *dialog base class constructor
     * @param context 上下文
     */
    public BaseDialog(@NonNull Context context) {
        super(context, R.style.quick_option_dialog);
        this.context = context;
    }

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //By default the dialog is displayed at the bottom of the layout
        getWindow().setGravity(Gravity.BOTTOM);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        //set dialog width
        p.width = d.getWidth();
        getWindow().setAttributes(p);
        //Click outside the dialog to close by default
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    /**
     * Get the resource file under the string package
     * @param resource resource file
     */
    public String  getString(int resource){
        return context.getResources().getString(resource);
    }

    /**
     * Show toast
     * @param content the content of the toast
     */
    public void showToast(String content) {
        if (toast == null) {
            //Display time is long
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        //hide keyboard
        hideKeyboard();
        //exhibit
        toast.show();
    }


    /**
     * hide keyboard
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && this.getCurrentFocus() != null) {
            if (this.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
