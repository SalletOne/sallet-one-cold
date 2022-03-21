package com.sallet.cold;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.sallet.cold.R;
import androidx.annotation.Nullable;

import com.sallet.cold.base.BaseActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hk.offline.utils.Mnemonic;
import com.sallet.cold.utils.BitmapUtils;
import com.sallet.cold.utils.DeflaterUtils;

import java.util.Hashtable;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 *离线端生成交易签名二维码,该二维码用于在线端第三步去扫描
 *The offline terminal generates a transaction signature QR code,
 * which is used to scan the online terminal in the third step
 */
public class ScanResultOkActivity extends BaseActivity {


    @InjectView(R.id.iv_ma)
    ImageView ivMa;
    @InjectView(R.id.sp_iv_1)
    ImageView spIv1;
    String sign;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result_ok);


        ButterKnife.inject(this);
        if(App.getSpString(App.isTracFirst)==null){
            //如果第一次进显示引导页
            //If the boot page is displayed for the first time
            spIv1.setVisibility(View.VISIBLE);
            App.saveString(App.isTracFirst,"false");
            spIv1.setOnClickListener(v-> spIv1.setVisibility(View.GONE));
        }else {
            spIv1.setVisibility(View.GONE);
        }
        //从上个页面获取签名字符串
        //Get the signature string from the previous page
        sign=getIntent().getStringExtra("sign");
        Log.e("sign",sign);
        //压缩签名字符串 Compressed signature
        sign= DeflaterUtils.zipString(sign);
        //在UI上展示出签名的二维码
        //Display the signed QR code on the UI
        ivMa.setImageBitmap(BitmapUtils.createQRCodeBitmap("hash:"+sign, 700, 700,"UTF-8","L", "1"));
        new Mnemonic().wordList();
    }

    @OnClick({R.id.rl_back, R.id.bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                //结束当前页面
                //end current page
                finish();
                break;
            case R.id.bt:
                //返回首页
                //Back to Home
                startActivity(new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }



}
