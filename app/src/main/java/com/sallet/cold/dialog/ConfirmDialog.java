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
import butterknife.InjectView;

/**
 * 倒计时3s展示交易重要信息,3s后自动关闭
 * Countdown 3s to display important transaction information, and automatically close after 3s
 */
public class ConfirmDialog extends BaseDialog {
    /**
     * 绑定UI
     *  bind ui
     */
    @InjectView(R.id.tv_send_addr)
    TextView tvSendAddr;
    @InjectView(R.id.tv_get_addr)
    TextView tvGetAddr;
    @InjectView(R.id.tv_num)
    TextView tvNum;
    @InjectView(R.id.tv_fee)
    TextView tvFee;
    @InjectView(R.id.tv_time)
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
            //页面消失
            //The timer counts down 3s
            dismiss();

        }

    };
    /**
     * @param context 上下文
     */
    public ConfirmDialog(@NonNull @NotNull Context context,String send,String receive,String num,String fee) {
        super(context);
        View convertView = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        //设置布局
        //set layout
        setContentView(convertView);
        ButterKnife.inject(this, convertView);
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
