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
 * 助记词备份再次确认弹窗
 * Mnemonic backup reconfirmation popup
 */
public class SureDialog extends Dialog {
    Context context;//上下文
    String words;//助记词

    /**
     * 构造方法
     * @param context 上下文
     * @param words 助记词
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
     * 带主题的构造方法
     * @param context 上下文
     * @param themeResId 主题
     * Constructor with theme
     * @param context context
     * @param themeResId theme
     */
    public SureDialog(Context context, int themeResId) {
        super(context, themeResId);
        //获取布局
        //get layout
        View convertView = getLayoutInflater().inflate(R.layout.dialog_make_sure, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置布局
        //set layout
        setContentView(convertView);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //默认显示在布局底部
        //Displays at the bottom of the layout by default
        getWindow().setGravity(Gravity.BOTTOM);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        //设置布局属性
        //Set layout properties
        getWindow().setAttributes(p);
        //确定按钮点击事件
        //OK button click event
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定启动下一页面,并把助记词传递给下个页面
                //Make sure to start the next page and pass the mnemonic to the next page
                context.startActivity(new Intent(context, ResumeWordActivity.class).putExtra("words",words));

            }
        });
        //关闭按钮点击事件
        //close button click event
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭
                //close
                dismiss();

            }
        });
    }
}
