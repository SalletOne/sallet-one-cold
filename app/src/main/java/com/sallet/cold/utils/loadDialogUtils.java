package com.sallet.cold.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sallet.cold.R;
import com.sallet.cold.dialog.LoadingDialog;


/**
 * Load Animation Popup
 */

public class loadDialogUtils {

    //Show loading animation
    public static Dialog createLoadingDialog(Context context, String msg) {

        LoadingDialog dialog=new LoadingDialog(context,msg);
        dialog.show();
        return dialog;
    }

    /**
     *
     *Close loading animation
     * @param mDialog
     */
    public static void closeDialog(Dialog mDialog) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

}
