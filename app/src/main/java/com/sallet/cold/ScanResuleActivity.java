/*
 * Copyright 2022 salletone developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sallet.cold;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hk.common.dto.BtcTransDTO;
import com.hk.common.dto.EthTransDTO;
import com.hk.common.dto.FilMsgDTO;
import com.hk.common.dto.FilTransDTO;
import com.hk.common.dto.SolTransDTO;
import com.hk.common.dto.TronTransDTO;
import com.hk.common.dto.XrpTransDTO;
import com.hk.common.utils.TxnType;
import com.hk.offline.base.SeedMasterKey;

import com.hk.offline.currency.Avax;
import com.hk.offline.currency.Bitcoin;
import com.hk.offline.currency.BitcoinCash;
import com.hk.offline.currency.Dogecoin;
import com.hk.offline.currency.Ethereum;
import com.hk.offline.currency.Filecoin;
import com.hk.offline.currency.Litecoin;
import com.hk.offline.currency.Matic;
import com.hk.offline.currency.Solana;
import com.hk.offline.currency.Tron;
import com.hk.offline.currency.Xrp;
import com.hk.offline.dto.AddressDTO;
import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.bean.ScanResultTradeBean;
import com.sallet.cold.dialog.ConfirmDialog;
import com.sallet.cold.dialog.PwDialog;
import com.sallet.cold.utils.AesUtils;
import com.sallet.cold.utils.ScanRuleUtil;

import org.bitcoinj.crypto.DeterministicHierarchy;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 通过扫描在线端的二维码的内容，在此页面展示交易信息
 * By scanning the content of the QR code on the online side,
 * the transaction information is displayed on this page
 */
public class ScanResuleActivity extends BaseActivity {

    String result;//在线端扫描的结果数据 Result data of online scan
    /**
     * 绑定UI
     * Bind UI
     */
    @InjectView(R.id.tv_send_addr)
    TextView tvSendAddr;
    @InjectView(R.id.tv_get_addr)
    TextView tvGetAddr;
    @InjectView(R.id.tv_num)
    TextView tvNum;
    @InjectView(R.id.tv_fee)
    TextView tvFee;
    @InjectView(R.id.bt)
    TextView Bt;
    String sendAddr;//发送地址 sending address
    String getAddr;//接受地址 Acceptance address
    String num;//交易数量 Number of transactions
    String fee;//矿工费 miner fee
    String sign;//交易签名 transaction signature
    int index=0;
    int type;
    private BtcTransDTO btcTrade;//btc交易相关内容 btc transaction related content
    private EthTransDTO ethTrade;//eth交易相关内容 Eth transaction related content
    private MyTask mTask;//子线程 child thread
    boolean success=true;//是否签名成功 Whether the signature is successful

    String message;//错误信息 error message
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        ButterKnife.inject(this);
        //离线端的扫描结果
        //Offline scan results
        result = getIntent().getStringExtra("result");
        //解析在线端传递过来的数据
        //Parse the data passed from the online end
        parseData();
        tvSendAddr.setText(sendAddr);
        tvGetAddr.setText(getAddr);


    }


    /**
     * 解析二维码数据，给相关数据赋值
     * 根据代币种类不同，从不同的参数中取出数据
     * Parse QR code data and assign values to related data
     * Extract data from different parameters according to different types of tokens
     */

    private void parseData(){
        try {
            ScanResultTradeBean bean= ScanRuleUtil.analysisTradeTCode(result);
            //代币种类
            //Token Type
            type= Integer.parseInt(bean.getType());
            /*
               根据代币种类给控件赋值发送地址，接收地址，交易数量和矿工费
                Assign the sending address, receiving address,
                transaction quantity and miner fee to the control according to the token type
             */
            switch (bean.getType()) {
                case "0": {
                    btcTrade = bean.getBtcTrade();
                    sendAddr = btcTrade.getSendAddress();
                    getAddr = btcTrade.getReceiveAddress();
                    for (BtcTransDTO.Out bb : btcTrade.getReceivers()) {
                        if (bb.getAddress().equalsIgnoreCase(getAddr)) {
                            num = bb.getAmount().toString();
                        }
                    }
                    fee = btcTrade.getFee().toString();
                    DecimalFormat df = new DecimalFormat("0.00000000");
                    String amount = df.format(Double.parseDouble(num) * 0.00000001);
                    tvNum.setText(amount + " BTC");
                    String amountFee = df.format(Double.parseDouble(fee) * 0.00000001);
                    tvFee.setText(amountFee + " BTC");
                    break;
                }
                case "1": {
                    ethTrade = bean.getEthTrade();
                    sendAddr = ethTrade.getSender();
                    getAddr = ethTrade.getReceiver();
                    num = ethTrade.getAmount().toPlainString();
                    fee = String.valueOf(ethTrade.getGasPrice());

                    DecimalFormat df = new DecimalFormat("0.000000");
                    tvNum.setText(num + " ETH");
                    String amountFee = df.format(Double.parseDouble(fee) * 21000 * 0.000000001);
                    tvFee.setText(amountFee + " ETH");

                    break;
                }

                default:

                    break;
            }
        }catch ( Exception e){
            showToast(getString(R.string.not_new_version));
        }


    }


    /**
     * 开启子线程去做耗时签名
     * Open child thread to do time-consuming signature
     */

    private class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected String doInBackground(String... params) {
            //获取助记词
            //get mnemonic
            String[] word = AesUtils.aesDecrypt(App.getSpString(App.word)).split(",");


            //通过助记符获取签名
            //Get signature by mnemonic
            DeterministicHierarchy dh = SeedMasterKey.seedMasterKey( Arrays.asList(word));

            try {
                //签名操作,根据每个代币的种类去调用不同的签名方法
                //Signature operation, calling different signature methods according to the type of each token
                switch (type) {
                    case 0:
                        sign = Bitcoin.getInstance().signTx(btcTrade, dh, index);
                        break;
                    case 1:
                        sign = Ethereum.getInstance().signTx(ethTrade, dh, index);
                        break;

                }
            } catch (Exception e) {


                success=false;
                message=e.getMessage();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progresses) {



        }

        @Override
        protected void onPostExecute(String result) {



            //取消加载
            //Cancel loading
            cancleLoading();
            if(!success){
                //如果签名失败提示错误
                //If the signature fails, it will prompt an error
                showToast(message);
                success=true;
            }else {
                //签名成功，启动下一个页面展示签名二维码
                //If the signature is successful, start the next page to display the signature QR code
                context.startActivity(new Intent(context, ScanResultOkActivity.class).putExtra("sign", sign));
            }
        }

        @Override
        protected void onCancelled() {



        }
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
                if(index==-1){
                    showToast(getStringResources(R.string.scan_resulte_ac1));
                }else {

                    //输入密码完成签名
                    //Enter the password to complete the signature
                    new PwDialog(context, getStringResources(R.string.input_password_2_sign), new PwDialog.OnPress() {
                        @Override
                        public void onPress() {

                            //密码正确启动子线程去签名
                            //The password is correct to start the child thread to sign
                            mTask = new MyTask();
                            mTask.execute();
                            //展示加载动画
                            //Show loading animation
                            showLoading();



                        }
                    },getAddr).show();

                    //再次确认关键交易数据弹窗
                    //Reconfirm the key transaction data popup
                    new ConfirmDialog(
                            context,tvSendAddr.getText().toString(),
                            tvGetAddr.getText().toString(),tvNum.getText().toString(),tvFee.getText().toString()).
                            show();
                }
                break;

        }
    }
}
