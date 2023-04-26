package com.sallet.cold.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sallet.cold.R;

import org.jetbrains.annotations.NotNull;

/**
 * toolTip box
 */
public class SubmitDialog extends BaseDialog{
    String content;
    /**
     * dialog base class constructor
     *
     */
    public SubmitDialog(@NonNull @NotNull Context context,String content) {
        super(context);
        this.content=content;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.CENTER);
        setContentView(R.layout.dialog_submit);
        TextView tvContent=findViewById(R.id.tv_content);
        tvContent.setText(content);

    }
}
