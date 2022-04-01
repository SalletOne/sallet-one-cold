package com.sallet.cold.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sallet.cold.R;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Countdown 3s to display important transaction information, and automatically close after 3s
 */
public class ConfirmDialog extends BaseDialog {
    /**
     *  bind ui
     */
    @BindView(R.id.tv_send_addr)
    TextView tvSendAddr;
    @BindView(R.id.tv_get_addr)
    TextView tvGetAddr;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.tv_time)
    TextView tvTime;
    /**
     * The timer counts down 3s
     */
    CountDownTimer cdt = new CountDownTimer(3*1000, 1000)//参数1：计时总时间，参数2：每次扣除时间数

    {

        @Override

        public void onTick(long millisUntilFinished) {
            tvTime.setText(getString(R.string.confirm_trac)+" "+(millisUntilFinished+1000)/1000+"s");
        }

        @Override

        public void onFinish() {
            //The timer counts down 3s
            dismiss();

        }

    };
    /**
     * @param context
     */
    public ConfirmDialog(@NonNull @NotNull Context context,String send,String receive,String num,String fee) {
        super(context);
        View convertView = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        //set layout
        setContentView(convertView);
        ButterKnife.bind(this, convertView);
        tvSendAddr.setText(send);
        tvGetAddr.setText(receive);
        tvNum.setText(num);
        tvFee.setText(fee);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.CENTER);
        cdt.start();
    }
}
