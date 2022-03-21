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
import com.sallet.cold.utils.ScanActivity;
import com.sallet.cold.utils.ScanRuleUtil;

import org.bitcoinj.crypto.DeterministicHierarchy;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * APP首页 ，展示已经生成的钱包地址列表， 点击每个选项展示该地址的二维码， 底部的扫一扫图标用来扫描在线端的交易二维码。
 *The homepage of the APP displays the list of wallet addresses that have been generated.
 * Click on each option to display the QR code of the address.
 * The scan icon at the bottom is used to scan the online transaction QR code.
 */


public class MainActivity extends BaseActivity {

    /**
     * 绑定UI
     */
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.rc_view)
    RecyclerView recyclerView;
    @InjectView(R.id.tvv_beifen)
    TextView tvv_beifen;

    List<CoinSetBean>list=new ArrayList<>();//地址列表 address list
    MainAdapter adapter=new MainAdapter(R.layout.item_main,list);
    String ETHAddress;//以太坊地址
    String BTCAddress;//比特币地址
    private MyTask mTask;//子线程 child thread
    List<String> words;//助记词 mnemonic


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //清除栈顶所有activity Clear all activities at the top of the stack
        ActivityCollector.finishAll();
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        if (App.getSpString(App.defautBTCAddress) != null) {
            //有默认地址直接从存储中取
            //There is a default address, which is taken directly from the storage table
            getAddress();
        } else {
            //
            //No default address, generate default BTC and ETH
            words = Arrays.asList(AesUtils.aesDecrypt(App.getSpString(App.word)).split(","));
            mTask = new MyTask();
            mTask.execute();
            showLoading();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        //保存登录状态
        // save login status
        App.saveString(App.isLogin, "true");

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //展示地址二维码
                //Display address QR code
                new AddressZxinDialog(context,list.get(position).getType(),list.get(position).getAddress()).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //获取地址
        // get address
        getAddress();



        tvv_beifen.setText(getStringResources(R.string.no_words_back_up));
        tvTitle.setText(getStringResources(R.string.ac_main_tv1));
    }


    /**
     * 保存生成过的地址
     * save the generated address
     */


    private void creatAddress() {
        //保存比特币地址为默认地址，用来区分是否已生成过地址
        //Save the bitcoin address as the default address to distinguish whether the address has been generated
        App.saveString(App.defautBTCAddress, BTCAddress);
        //保存币种相关信息 地址 类别
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
        //保存到APP中
        // Save to APP
        App.saveAddressList(list);


        adapter.notifyDataSetChanged();

    }

    /**
     * 获取地址如果APP中保存的地址不是空，清空下集合然后从APP的存储中更新钱包地址列表数据
     * isCheck返回true则在首页中展示
     * Get the address If the address saved in the APP is not empty,
     * clear the lower set and then update the wallet address list data from the storage of the APP
     * if isCheck returns true, it will be displayed on the home page
     */

    private void getAddress(){
        if(App.getAddressList()!=null) {
            //清空集合
            //empty collection
            list.clear();
            for (CoinSetBean bean :App.getAddressList()) {
                if (bean.isCheck()) {
                    //选中则加入集合中
                    //Select to add to the collection
                    list.add(bean);
                }
            }
            //更新UI
            //Update UI
            adapter.notifyDataSetChanged();
        }
    }





    @OnClick({R.id.iv_sao,R.id.iv_set})
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.iv_sao:
                //扫描在线端生成的交易二维码
                //Scan the online transaction code
                new IntentIntegrator(this)
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)

                        .setCameraId(0)
                        .setBeepEnabled(true)
                        .setCaptureActivity(ScanActivity.class)
                        .initiateScan();
                break;
            case R.id.iv_set:

                //主页弹窗
                //Homepage popup
                new MainDialog(context, new MainDialog.OnMainClick() {
                    @RequiresApi(api = Build.VERSION_CODES.R)
                    @Override
                    public void onClick(int position, int type) {
                        switch (position){
                            case 0: //币种设置Currency settings

                                startActivity(new Intent(context,CoinSetActivity.class));

                                break;

                            case 1://销毁钱包 destroy wallet
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
                            case 2://语言设置 language settings
                                startActivity(new Intent(context, LanguageActivity.class));

                                break;
                            case 3://关于我们 AboutUs
                                startActivity(new Intent(context, AboutUsActivity.class));

                                break;
                            case 4://USB更新版本 usb upgrade

                                startActivity(new Intent(context, UsbActivity.class));



                                break;
                            case 5://助记词备份 mnemonic backup

//                               main();
                                new PwDialog(context, "",  getStringResources(R.string.please_input_etpass), new PwDialog.OnPress() {
                                    @Override
                                    public void onPress() {
                                        String[] word = AesUtils.aesDecrypt(App.getSpString(App.word)).split(",");
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
        //扫描在线端交易二维码的结果
        // The result of scanning the online transaction QR code
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                //如果是空，则不处理
                // If empty, do not process
            } else {
                String result = intentResult.getContents();
                Log.e("result",result);
                //校验是否是交易二维码 result==1 是交易二维码，否则提示错误
                // Check whether it is a transaction QR code result==1 is a transaction QR code, otherwise it will prompt an error
                if(ScanRuleUtil.checkData(result)==1){
                    //是交易二维码开启新页面，把扫描出来的交易内容传给下个页面
                    //It is the transaction QR code to open a new page and pass the scanned transaction content to the next page
                    startActivity(new Intent(context, ScanResuleActivity.class).putExtra("result", result));
                }else {
                    showToast(getStringResources(R.string.qr_code_not_trade));
                }


            }
        }
    }

    /**
     * 打开子线程，生成地址
     *Open child thread, generate address
     */

    private class MyTask extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {

        }



        @Override
        protected String doInBackground(String... params) {


            DeterministicHierarchy dh = SeedMasterKey.seedMasterKey(words);
            //通过助记词生成地址
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
            //生成地址，取消加载
            //Generate address, cancel loading
            creatAddress();
            cancleLoading();
        }


        @Override
        protected void onCancelled() {



        }
    }



}
