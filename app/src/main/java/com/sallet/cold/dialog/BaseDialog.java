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
    //上下文
    //context
    Context context;

    /**
     * dialog基类构造方法
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
        //默认设置dialog显示在布局底部
        //By default the dialog is displayed at the bottom of the layout
        getWindow().setGravity(Gravity.BOTTOM);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        //设置dialog宽度
        //set dialog width
        p.width = d.getWidth();
        getWindow().setAttributes(p);
        //点击dialog外部默认关闭
        //Click outside the dialog to close by default
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    /**
     * 获取string包下的资源文件
     * @param resource 资源文件
     * Get the resource file under the string package
     * @param resource resource file
     */
    public String  getString(int resource){
        return context.getResources().getString(resource);
    }

    /**
     * 展示吐司
     * @param content 吐司的内容
     * Show toast
     * @param content the content of the toast
     */
    public void showToast(String content) {
        if (toast == null) {
            //展示时间为长时间
            //Display time is long
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        //隐藏键盘
        //hide keyboard
        hideKeyboard();
        //展示
        //exhibit
        toast.show();
    }


    /**
     * 隐藏键盘
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
