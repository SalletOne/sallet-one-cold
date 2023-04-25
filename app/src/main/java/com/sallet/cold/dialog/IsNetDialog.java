package com.sallet.cold.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;

import androidx.annotation.NonNull;

import com.sallet.cold.R;

import org.jetbrains.annotations.NotNull;

/**
 * Link Network Prompt Box
 */
public class IsNetDialog extends BaseDialog{
    /**
     * dialog base class constructor
     *
     * @param context 上下文
     */
    public IsNetDialog(Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.TOP);
        setContentView(R.layout.dialog_isnet);



    }
}
