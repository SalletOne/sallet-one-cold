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
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hk.common.dto.TronTransDTO;
import com.hk.offline.base.SeedMasterKey;
import com.hk.offline.currency.Bitcoin;
import com.hk.offline.currency.Ethereum;
import com.hk.offline.currency.Tron;
import com.hk.offline.dto.AddressDTO;
import com.sallet.cold.about.AboutUsActivity;
import com.sallet.cold.adapter.MainAdapter;
import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.bean.CoinSetBean;
import com.sallet.cold.dialog.AddressZxinDialog;
import com.sallet.cold.dialog.IsNetDialog;
import com.sallet.cold.dialog.MainDialog;
import com.sallet.cold.dialog.PwDeleteDialog;
import com.sallet.cold.dialog.PwDialog;
import com.sallet.cold.start.BackUpWordActivity;
import com.sallet.cold.start.StartActivity;
import com.sallet.cold.utils.ActivityCollector;
import com.sallet.cold.utils.AesUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.sallet.cold.utils.LanguageActivity;
import com.sallet.cold.utils.NetChangeReceiver;
import com.sallet.cold.utils.ScanActivity;
import com.sallet.cold.utils.ScanRuleUtil;

import org.bitcoinj.crypto.DeterministicHierarchy;
import org.web3j.crypto.CipherException;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 *The homepage of the APP displays the list of wallet addresses that have been generated.
 * Click on each option to display the QR code of the address.
 * The scan icon at the bottom is used to scan the online transaction QR code.
 */


public class MainActivity extends BaseActivity {

    /**
     */
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rc_view)
    RecyclerView recyclerView;
    @BindView(R.id.tvv_beifen)
    TextView tvv_beifen;

    List<CoinSetBean>list=new ArrayList<>();// address list
    MainAdapter adapter=new MainAdapter(R.layout.item_main,list);
    String ETHAddress;//
    String BTCAddress;//
    private MyTask mTask;// child thread
    List<String> words;// mnemonic
    IsNetDialog isNetDialog;//
    private NetChangeReceiver netBroadcastReceiver;
    /**
     *Check the network status to prompt users if the environment is secure
     */
    private int netType;
    private void checkNet() {
        netBroadcastReceiver = new NetChangeReceiver();
        netBroadcastReceiver.setNetChangeListener(new NetChangeReceiver.NetChangeListener() {
            @Override
            public void onChangeListener(int status) {
                netType = status;
                if (!isFinishing()&&isNetConnect()) {
                    isNetDialog.show();
                }else{
                    if (isNetDialog.isShowing()){
                        isNetDialog.dismiss();
                    }
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(netBroadcastReceiver, filter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Clear all activities at the top of the stack
        ActivityCollector.finishAll();
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        isNetDialog=new IsNetDialog(context);

        ButterKnife.bind(this);

        if (App.getSpString(App.defautBTCAddress) != null) {
            //There is a default address, which is taken directly from the storage table
            getAddress();
        } else {
            //No default address, generate default BTC and ETH
            words = Arrays.asList(AesUtils.aesDecrypt(getIntent().getStringExtra(App.password),App.getSpString(App.word)).split(","));
            mTask = new MyTask();
            mTask.execute();
            showLoading();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        // save login status
        App.saveString(App.isLogin, "true");

        adapter.setOnItemClickListener(new  OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Display address QR code
                new AddressZxinDialog(context,list.get(position).getType(),list.get(position).getAddress()).show();

            }
        });
        checkNet();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // get address
        getAddress();



        tvv_beifen.setText(getStringResources(R.string.no_words_back_up));
        tvTitle.setText(getStringResources(R.string.ac_main_tv1));
    }


    /**
     * save the generated address
     */


    private void creatAddress() {
        //Save the bitcoin address as the default address to distinguish whether the address has been generated
        App.saveString(App.defautBTCAddress, BTCAddress);
        // Save currency related information Address Category
        CoinSetBean address = new CoinSetBean();
        address.setAddress(BTCAddress);
        address.setName("BTC");
        address.setCheck(true);
        address.setType(0);
        list.add(address);

        CoinSetBean address1 = new CoinSetBean();
        address1.setAddress(ETHAddress);
        address1.setName("ETH");
        address1.setCheck(true);
        address1.setType(1);
        list.add(address1);
        // Save to APP
        App.saveAddressList(list);


        adapter.notifyDataSetChanged();

    }

    /**

     * Get the address If the address saved in the APP is not empty,
     * clear the lower set and then update the wallet address list data from the storage of the APP
     * if isCheck returns true, it will be displayed on the home page
     */

    private void getAddress(){
        if(App.getAddressList()!=null) {
            //empty collection
            list.clear();
            for (CoinSetBean bean :App.getAddressList()) {
                if (bean.isCheck()) {
                    //Select to add to the collection
                    list.add(bean);
                }
            }
            //Update UI
            adapter.notifyDataSetChanged();
        }
    }





    @OnClick({R.id.iv_sao,R.id.iv_set})
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.iv_sao:
                //Scan the online transaction code
                new IntentIntegrator(this)
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)

                        .setCameraId(0)
                        .setBeepEnabled(true)
                        .setCaptureActivity(ScanActivity.class)
                        .initiateScan();
                break;
            case R.id.iv_set:

                //Homepage popup
                new MainDialog(context, new MainDialog.OnMainClick() {
                    @Override
                    public void onClick(int position, int type) {
                        switch (position){
                            case 0: //Currency settings

                                startActivity(new Intent(context,CoinSetActivity.class));

                                break;

                            case 1:// destroy wallet
                                if(type==0){
                                    new PwDeleteDialog(context, new PwDeleteDialog.OnPress() {
                                        @Override
                                        public void onPress() {
                                            App.clear();
                                            startActivity(new Intent(context, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK));
                                        }
                                    }).show();
                                }else {
                                    startActivity(new Intent(context,ChangePassActivity.class));
                                }
                                break;
                            case 2:// language settings
                                startActivity(new Intent(context, LanguageActivity.class));

                                break;
                            case 3:// AboutUs
                                startActivity(new Intent(context, AboutUsActivity.class));

                                break;
                            case 4:// usb upgrade

                                startActivity(new Intent(context, UsbActivity.class));



                                break;
                            case 5:// mnemonic backup





                                new PwDialog(context, "",  getStringResources(R.string.please_input_etpass), new PwDialog.OnPress() {
                                    @Override
                                    public void onPress(String password) {
                                        String[] word = AesUtils.aesDecrypt(password,App.getSpString(App.word)).split(",");
                                        startActivity(new Intent(context, BackUpWordActivity.class).putExtra("root",1)
                                        .putExtra("words",word));

                                    }
                                }).show();

                                break;

                        }
                    }
                }).show();




                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // The result of scanning the online transaction QR code
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                // If empty, do not process
            } else {
                String result = intentResult.getContents();
                Log.e("result",result);
                // Check whether it is a transaction QR code result==1 is a transaction QR code, otherwise it will prompt an error
                if(ScanRuleUtil.checkData(result)==1){
                    //It is the transaction QR code to open a new page and pass the scanned transaction content to the next page
                    startActivity(new Intent(context, ScanResuleActivity.class).putExtra("result", result));
                }else {
                    showToast(getStringResources(R.string.qr_code_not_trade));
                }


            }
        }
    }

    /**
     *Open child thread, generate address
     */

    private class MyTask extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {

        }



        @Override
        protected String doInBackground(String... params) {


            DeterministicHierarchy dh = SeedMasterKey.seedMasterKey(words);
            //Generate address from mnemonic
            AddressDTO btcAddress = Bitcoin.getInstance().address(dh,0);
                AddressDTO ethAddress = Ethereum.getInstance().address(dh,0);
                BTCAddress=btcAddress.getAddress();
                ETHAddress=ethAddress.getAddress();







            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... progresses) {



        }


        @Override
        protected void onPostExecute(String result) {
            //Generate address, cancel loading
            creatAddress();
            cancleLoading();
        }


        @Override
        protected void onCancelled() {



        }
    }

    /**

     */
    public boolean isNetConnect() {
        if (netType == 1) {
            return true;
        } else if (netType == 0) {
            return true;
        } else if (netType == -1) {
            return false;
        }
        return false;
    }


}
