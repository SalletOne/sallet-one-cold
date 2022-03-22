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
 * 展示该地址的二维码，用于在线端扫描，扫描成功在在线端生成绑定该地址的钱包
 * Display the QR code of the address for online scanning, and successfully generate a wallet bound to the address online
 */
public class AddressZxinDialog extends BaseDialog {
    Context context;
    /**
     * 绑定UI
     * Bind UI
     */
    ImageView ivBack;
    ImageView iv_logo;
    TextView tv_logo;
    ImageView ivMa;
    TextView tvAddress;

    int type;//代币种类 Token Type
    String address;//代币地址 Token address

    /**
     * 构造方法 用于赋值该类内的对象
     * Constructor is used to assign objects within the class
     * @param context 上下文 context
     * @param type 代币种类 Token Type
     * @param address 代币地址 Token address
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
         * 绑定UI
         * Bind UI
         */
        ivBack=findViewById(R.id.iv_back);
        ivMa=findViewById(R.id.iv_ma);
        tvAddress=findViewById(R.id.tv_address);
        iv_logo=findViewById(R.id.iv_logo);
        tv_logo=findViewById(R.id.tv_logo);


        /*
         * 根据代币种类来显示UI
         * 图片 ，代币名称， 字体颜色
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
        }


            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            tvAddress.setText(address);
            //生成带有协议的地址
            //Generate address with protocol
            String scan= ScanRuleUtil.createAddressTCode(type,address);
            //把带有协议的地址生成二维码图片
            //Generate a QR code image from the address with the agreement
            ivMa.setImageBitmap(BitmapUtils.createQRCodeBitmap(scan, 600, 600,"UTF-8","L", "1"));

    }




}
