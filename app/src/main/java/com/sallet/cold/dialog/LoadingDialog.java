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
import android.widget.TextView;

import com.sallet.cold.R;

/**
 * 用来展示加载动画的dialog
 * The dialog used to show the loading animation
 */
public class LoadingDialog extends Dialog {
    //上下文
    //context
    Context context;
    //展示的文字
    //displayed text
    String words;


    /**
     * loading 构造方法
     * @param context    //上下文
     * @param words  //展示的文字
     * loading constructor
     * @param context //Context
     * @param words //The displayed text
     */
    public LoadingDialog(Context context, String words) {
        this(context, R.style.quick_option_dialog);
        //赋值
        //assign
        this.context=context;
        this.words=words;
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        //加载布局
        //load layout
        View convertView = getLayoutInflater().inflate(R.layout.dialog_loading1, null);
        TextView textView=convertView.findViewById(R.id.tv_loading);
        textView.setText(words);
        //设置布局
        //set layout
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(convertView);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //设置布局居中显示
        //Set the layout to be centered
        getWindow().setGravity(Gravity.CENTER);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        //设置在窗口中的显示属性
        //Set the display properties in the window
        getWindow().setAttributes(p);

    }
}
