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
import com.hk.common.dto.TerraTransDTO;
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
import com.sallet.cold.bean.CoinSetBean;
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
import butterknife.BindView;
import butterknife.OnClick;
import cn.hutool.core.util.ArrayUtil;

/**
 * By scanning the content of the QR code on the online side,
 * the transaction information is displayed on this page
 */
public class ScanResuleActivity extends BaseActivity {

    String result;// Result data of online scan
    /**
     * Bind UI
     */
    @BindView(R.id.tv_send_addr)
    TextView tvSendAddr;
    @BindView(R.id.tv_get_addr)
    TextView tvGetAddr;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.bt)
    TextView Bt;
    String sendAddr;// sending address
    String getAddr;// Acceptance address
    String num;// Number of transactions
    String fee;// miner fee
    String sign;// transaction signature
    String time;//时间戳
    int index=0;
    int type;
    private BtcTransDTO btcTrade;// btc transaction related content
    private FilMsgDTO filTrade;// fil transaction related content
    private EthTransDTO ethTrade;// Eth transaction related content
    private XrpTransDTO xrpTransDTO;// xrp transaction related content
    private SolTransDTO solTransDTO;// sol transaction related content
    private TronTransDTO tronTransDTO;// tron transaction related content
    private TerraTransDTO terraTransDTO;// tron transaction related content
    private MyTask mTask;// child thread
    boolean success=true;// Whether the signature is successful

    String message;// error message
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        ButterKnife.bind(this);
        //Offline scan results
        result = getIntent().getStringExtra("result");
        //Parse the data passed from the online end
        parseData();
        tvSendAddr.setText(sendAddr);
        tvGetAddr.setText(getAddr);


    }


    /**
     * Parse QR code data and assign values to related data
     * Extract data from different parameters according to different types of tokens
     */

    private void parseData(){
        try {
            ScanResultTradeBean bean= ScanRuleUtil.analysisTradeTCode(result);
            //Token Type
            type= Integer.parseInt(bean.getType());
            time=bean.getTime();
            /*
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
                case "2": {
                    ethTrade = bean.getEthTrade();
                    sendAddr = ethTrade.getSender();
                    getAddr = ethTrade.getReceiver();
                    num = String.valueOf(ethTrade.getAmount());
                    fee = String.valueOf(ethTrade.getGasPrice());

                    DecimalFormat df = new DecimalFormat("0.000000");
                    tvNum.setText(num + " "+ethTrade.getSymbol());
                    String amountFee = df.format(Double.parseDouble(fee) * ethTrade.getGasLimit() * 0.000000001);
                    tvFee.setText(amountFee + " ETH");

                    break;
                }
                case "4": {
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
                    tvNum.setText(amount + " DOGE");
                    String amountFee = df.format(Double.parseDouble(fee) * 0.00000001);
                    tvFee.setText(amountFee + " DOGE");

                    break;
                }

                case "5": {
                    btcTrade = bean.getBtcTrade();
                    sendAddr = btcTrade.getSendAddress();
                    getAddr = btcTrade.getReceiveAddress();

                    fee = btcTrade.getFee().toString();
                    for (BtcTransDTO.Out bb : btcTrade.getReceivers()) {
                        if (bb.getAddress().equalsIgnoreCase(getAddr)) {
                            num = bb.getAmount().toString();
                        }
                    }
                    DecimalFormat df = new DecimalFormat("0.00000000");
                    String amount = df.format(Double.parseDouble(num) * 0.00000001);
                    tvNum.setText(amount + " BCH");
                    String amountFee = df.format(Double.parseDouble(fee) * 0.00000001);
                    tvFee.setText(amountFee + " BCH");

                    break;
                }
                case "6": {
                    btcTrade = bean.getBtcTrade();
                    sendAddr = btcTrade.getSendAddress();
                    getAddr = btcTrade.getReceiveAddress();

                    fee = btcTrade.getFee().toString();
                    for (BtcTransDTO.Out bb : btcTrade.getReceivers()) {
                        if (bb.getAddress().equalsIgnoreCase(getAddr)) {
                            num = bb.getAmount().toString();
                        }
                    }
                    DecimalFormat df = new DecimalFormat("0.00000000");
                    String amount = df.format(Double.parseDouble(num) * 0.00000001);
                    tvNum.setText(amount + " LTC");
                    String amountFee = df.format(Double.parseDouble(fee) * 0.00000001);
                    tvFee.setText(amountFee + " LTC");

                    break;
                }
                case "7": {
                    filTrade = bean.getFilTrade();
                    sendAddr = filTrade.getFilTransDTO().getFrom();
                    getAddr = filTrade.getFilTransDTO().getTo();

                    fee = filTrade.getFee().toString();
                    num=filTrade.getFilTransDTO().getValue();
                    DecimalFormat df = new DecimalFormat("0.00000000");
                    String amount = df.format(Double.parseDouble(num) /Math.pow(10,18));
                    tvNum.setText(amount + " FIL");
                    String amountFee = df.format(Double.parseDouble(fee) * 2200000 * 0.000000001);
                    tvFee.setText(amountFee + " FIL");

                    break;
                }


                case "8": {
                    ethTrade = bean.getEthTrade();
                    sendAddr = ethTrade.getSender();
                    getAddr = ethTrade.getReceiver();
                    num = ethTrade.getAmount().toPlainString();
                    fee = String.valueOf(ethTrade.getGasPrice());

                    DecimalFormat df = new DecimalFormat("0.000000");
                    tvNum.setText(num + " MATIC");
                    String amountFee = df.format(Double.parseDouble(fee) * 21000 * 0.000000001);
                    tvFee.setText(amountFee + " MATIC");


                    break;
                }


                case "11":
                    xrpTransDTO = bean.getXrpTransDTO();
                    sendAddr = xrpTransDTO.getSender();
                    getAddr = xrpTransDTO.getReceiver();
                    fee = String.valueOf(xrpTransDTO.getFee());
                    tvNum.setText(xrpTransDTO.getAmount()*0.000001 + " XRP");
                    tvFee.setText(fee + " drops");

                    break;
                case "12":
                    solTransDTO = bean.getSolTransDTO();
                    sendAddr = solTransDTO.getSender();
                    getAddr = solTransDTO.getReceiver();
                    fee = "0.000005";
                    tvNum.setText(solTransDTO.getLamports()/Math.pow(10,9) + " SOL");
                    tvFee.setText(fee + " SOL");

                    break;
                case "13":
                    ethTrade = bean.getEthTrade();
                    sendAddr = ethTrade.getSender();
                    getAddr = ethTrade.getReceiver();
                    num = ethTrade.getAmount().toPlainString();
                    fee = String.valueOf(ethTrade.getGasPrice());

                    DecimalFormat df = new DecimalFormat("0.000000");
                    tvNum.setText(num + " AVAX");
                    String amountFee = df.format(Double.parseDouble(fee) * 21000 * 0.000000001);
                    tvFee.setText(amountFee + " AVAX");


                    break;
                default:

                    break;
            }
        }catch ( Exception e){
            showToast(getString(R.string.not_new_version));
        }
        boolean isHav=false;
        for (CoinSetBean bean :App.getAddressList()) {
            if (bean.getAddress().equals(sendAddr)) {
                isHav=true;
            }
        }
        if(!isHav){
            showToast(getString(R.string.address_not_cold));
            finish();
        }



    }


    /**
     * Open child thread to do time-consuming signature
     */

    private class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected String doInBackground(String... params) {
            //get mnemonic
            String[] word = AesUtils.aesDecrypt(pass,App.getSpString(App.word)).split(",");


            //Get signature by mnemonic
            DeterministicHierarchy dh = SeedMasterKey.seedMasterKey( Arrays.asList(word));

            try {
                //Signature operation, calling different signature methods according to the type of each token
                switch (type) {
                    case 0:
                        sign = Bitcoin.getInstance().signTx(btcTrade, dh, index);
                        break;
                    case 1:
                        sign = Ethereum.getInstance().signTx(ethTrade, dh, index);
                        break;
                    case 2:
                        sign = Ethereum.getInstance().erc20Sign(ethTrade, dh, index);
                        break;
                    case 4:
                        sign = Dogecoin.getInstance().signTx(btcTrade, dh, index);
                        break;
                    case 5:
                        sign = BitcoinCash.getInstance().signTx(btcTrade, dh, index);
                        break;
                    case 6:
                        sign = Litecoin.getInstance().signTx(btcTrade, dh, index);
                        break;
                    case 7:
                        sign = Filecoin.getInstance().sign(filTrade.getFilTransDTO(), dh, index);
                        break;
                    case 8:
                        sign = Matic.getInstance().signTx(ethTrade, dh, index);
                        break;
                    case 11:
                        sign = Xrp.getInstance().signTx(xrpTransDTO, dh, index);
                        break;
                    case 13:
                        sign = Avax.getInstance().signTx(ethTrade, dh, index);
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



            //Cancel loading
            cancleLoading();
            if(!success){
                //If the signature fails, it will prompt an error
                showToast(message);
                success=true;
            }else {
                //If the signature is successful, start the next page to display the signature QR code
                context.startActivity(new Intent(context, ScanResultOkActivity.class).putExtra("sign", sign)
                        .putExtra("time",time));
            }
        }

        @Override
        protected void onCancelled() {



        }
    }
    String pass;
    @OnClick({R.id.rl_back, R.id.bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                //end current page
                finish();
                break;
            case R.id.bt:
                if(index==-1){
                    showToast(getStringResources(R.string.scan_resulte_ac1));
                }else {

                    //Enter the password to complete the signature
                    new PwDialog(context, getStringResources(R.string.input_password_2_sign), new PwDialog.OnPress() {
                        @Override
                        public void onPress(String password) {
                            pass=password;
                            //The password is correct to start the child thread to sign
                            mTask = new MyTask();
                            mTask.execute();
                            //Show loading animation
                            showLoading();



                        }
                    },getAddr).show();

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
