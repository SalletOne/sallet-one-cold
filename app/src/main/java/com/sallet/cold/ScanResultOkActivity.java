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
import butterknife.BindView;
import butterknife.OnClick;


/**
 *The offline terminal generates a transaction signature QR code,
 * which is used to scan the online terminal in the third step
 */
public class ScanResultOkActivity extends BaseActivity {


    @BindView(R.id.iv_ma)
    ImageView ivMa;
    @BindView(R.id.sp_iv_1)
    ImageView spIv1;
    String sign;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result_ok);


        ButterKnife.bind(this);
        if(App.getSpString(App.isTracFirst)==null){
            //If the boot page is displayed for the first time
            spIv1.setVisibility(View.VISIBLE);
            App.saveString(App.isTracFirst,"false");
            spIv1.setOnClickListener(v-> spIv1.setVisibility(View.GONE));
        }else {
            spIv1.setVisibility(View.GONE);
        }
        //Get the signature string from the previous page
        sign=getIntent().getStringExtra("sign");
        Log.e("sign",sign);
        // Compressed signature
        sign= DeflaterUtils.zipString(sign);
        //Display the signed QR code on the UI
        ivMa.setImageBitmap(BitmapUtils.createQRCodeBitmap("hash:"+sign, 700, 700,"UTF-8","L", "1"));
        new Mnemonic().wordList();
    }

    @OnClick({R.id.rl_back, R.id.bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                //end current page
                finish();
                break;
            case R.id.bt:
                //Back to Home
                startActivity(new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }



}
