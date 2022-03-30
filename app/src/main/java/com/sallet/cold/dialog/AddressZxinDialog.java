package com.sallet.cold.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sallet.cold.App;

import com.sallet.cold.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sallet.cold.utils.BitmapUtils;
import com.sallet.cold.utils.ScanRuleUtil;

import java.util.Hashtable;

/**
 * Display the QR code of the address for online scanning, and successfully generate a wallet bound to the address online
 */
public class AddressZxinDialog extends BaseDialog {
    Context context;
    /**
     * Bind UI
     */
    ImageView ivBack;
    ImageView iv_logo;
    TextView tv_logo;
    ImageView ivMa;
    TextView tvAddress;

    int type;// Token Type
    String address;// Token address

    /**
     * Constructor is used to assign objects within the class
     * @param context  context
     * @param type  Token Type
     * @param address  Token address
     */
    public AddressZxinDialog(Context context,int  type,String address) {
        super(context);
        this.context = context;
        this.type = type;
        this.address = address;
        View convertView = getLayoutInflater().inflate(R.layout.dialog_address_zxing, null);
        setContentView(convertView);
    }



    @Override
    protected void onCreate(Bundle bundle) {
            super.onCreate(bundle);
        getWindow().setGravity(Gravity.BOTTOM);
        /**
         * Bind UI
         */
        ivBack=findViewById(R.id.iv_back);
        ivMa=findViewById(R.id.iv_ma);
        tvAddress=findViewById(R.id.tv_address);
        iv_logo=findViewById(R.id.iv_logo);
        tv_logo=findViewById(R.id.tv_logo);


        /*
         * Display UI according to token type
         * Picture, token name, font color
         */

        switch (type){
            case 0:

                iv_logo.setBackgroundResource(R.mipmap.ic_btc_logo);
                tv_logo.setText("BTC");
                tv_logo.setTextColor(Color.parseColor("#EC871E"));
                break;
            case 1:
                iv_logo.setBackgroundResource(R.mipmap.ic_set_eth);
                tv_logo.setText("ETH");
                tv_logo.setTextColor(Color.parseColor("#1652F0"));
                break;
            case 2:
                iv_logo.setBackgroundResource(R.mipmap.ic_dog_logo);
                tv_logo.setText("DOGE");
                tv_logo.setTextColor(Color.parseColor("#BA9F33"));
                break;
            case 3:
                iv_logo.setBackgroundResource(R.mipmap.ic_set_bch);
                tv_logo.setText("BCH");
                tv_logo.setTextColor(Color.parseColor("#48CB45"));
                break;
            case 4:
                iv_logo.setBackgroundResource(R.mipmap.ic_set_ltc);
                tv_logo.setText("LTC");
                tv_logo.setTextColor(Color.parseColor("#345C99"));
                break;
        }


            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            tvAddress.setText(address);
            //Generate address with protocol
            String scan= ScanRuleUtil.createAddressTCode(type,address);
            //Generate a QR code image from the address with the agreement
            ivMa.setImageBitmap(BitmapUtils.createQRCodeBitmap(scan, 600, 600,"UTF-8","L", "1"));

    }




}
