package com.sallet.cold;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hk.offline.base.SeedMasterKey;
import com.hk.offline.currency.Bitcoin;
import com.hk.offline.currency.BitcoinCash;
import com.hk.offline.currency.Dogecoin;
import com.hk.offline.currency.Ethereum;
import com.hk.offline.currency.Filecoin;
import com.hk.offline.currency.Litecoin;
import com.hk.offline.currency.Solana;
import com.hk.offline.currency.Tron;
import com.hk.offline.currency.Xrp;
import com.hk.offline.dto.AddressDTO;
import com.sallet.cold.adapter.CoinSetAdapter;
import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.bean.CoinSetBean;
import com.sallet.cold.dialog.PwDialog;
import com.sallet.cold.polket.PolAddress;
import com.sallet.cold.polket.PolKatUtils;
import com.sallet.cold.utils.AesUtils;
import com.sallet.cold.utils.RecycleViewDivider;

import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.crypto.DeterministicHierarchy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 币种地址管理
 * Currency address management
 */
public class CoinSetActivity extends BaseActivity {


    @InjectView(R.id.rl_back)
    RelativeLayout rlBack;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    @InjectView(R.id.rc_view)
    RecyclerView rcView;
    List<CoinSetBean> list=new ArrayList<>();//地址列表 address list
    CoinSetAdapter coinSetAdapter;//地址列表适配器 address list adapter
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_set);
        ButterKnife.inject(this);
        //每个item之间的分割线
        //dividing line between each item
        rcView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.VERTICAL));

        coinSetAdapter=new CoinSetAdapter(R.layout.item_coin_set,list);
        rcView.setLayoutManager(new LinearLayoutManager(context));
        rcView.setAdapter(coinSetAdapter);
        //adapter添加列表点击事件
        //adapter add list click event
        coinSetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //是否选中
                //Is it selected
                boolean check=list.get(position).isCheck();
                //获取已生成过并保存的地址
                //Get the address that has been generated and saved
                List<CoinSetBean>addressList=App.getAddressList();
                //地址是否生成过
                //whether the address has been generated
                boolean isHave=false;
                for(int i=0;i<addressList.size();i++){
                    CoinSetBean bean=addressList.get(i);
                    if(bean.getType()==position){
                        isHave=true;
                        //设置开启状态
                        //set open state
                        bean.setCheck(!check);
                        //修改集合中该位置的开启状态
                        //Modify the open state of this location in the collection
                        addressList.set(i,bean);
                        //保存更新的集合
                        //Save the updated collection
                        App.saveAddressList(addressList);
                    }
                }

                if(!isHave&&!check) {
                    //未生成未选中则生成地址
                    //Not Generated If not selected, generate the address
                    creatAddress(position);
                }else {
                    //已生成直接更改对象状态
                    //Generated direct change object state
                    list.get(position).setCheck(!check);

                }
                //更新UI
                //Update UI
                coinSetAdapter.notifyDataSetChanged();
            }
        });
        //初始化数据
        //Initialization data
        initData();
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    /**
     * 初始化币种数据，把币种数据加入到集合中
     * Initialize the currency data and add the currency data to the collection
     */


    private void initData(){

        CoinSetBean data=new CoinSetBean();
        data.setName("Bitcoin");
        data.setNameSign("BTC");
        data.setImage(R.mipmap.ic_set_btc);
        list.add(data);
        CoinSetBean data1=new CoinSetBean();
        data1.setName("Ethereum");
        data1.setNameSign("ETH");
        data1.setImage(R.mipmap.ic_set_eth);
        list.add(data1);
        CoinSetBean data2=new CoinSetBean();

//        CoinSetBean data9=new CoinSetBean();
//        data9.setName("Polokadot");
//        data9.setNameSign("DOT");
//        data9.setImage(R.mipmap.ic_set_dot);
//        list.add(data9);
        List<CoinSetBean>addressList=App.getAddressList();
        //初始化已保存的数据
        //Initialize saved data
        for(int i=0;i<addressList.size();i++){ //
                CoinSetBean bean=list.get(addressList.get(i).getType());
                bean.setCheck(addressList.get(i).isCheck());
                list.set(addressList.get(i).getType(),bean);
        }




        coinSetAdapter.notifyDataSetChanged();
    }


    /**
     * 通过助记词生成生成地址
     * Generate address by mnemonic generation
     * @param type 币种
     */


    private void creatAddress(int type){
        //如果全局已保存密码不为空，则直接生成地址
        //If the global saved password is not empty, the address will be generated directly
        if(App.passWord.length()>0) {
            //通过密码获取助记词
            //Get mnemonic by password
            String[] word = AesUtils.aesDecrypt(App.getSpString(App.word)).split(",");
            Arrays.asList(word);
            //通过助记词和币种生成地址
            //Generate address from mnemonic and currency
            creatAddress2(word,type);
            //设置选中
            //set checked
            list.get(type).setCheck(true);
            //更新UI状态
            //Update UI state
            coinSetAdapter.notifyDataSetChanged();
        }else {
            //若全局保存密码为空，则输入正确的密码后才能生成地址
            //If the global save password is empty, the address can only be generated after entering the correct password.
            new PwDialog(context, "",  getStringResources(R.string.please_input_etpass), new PwDialog.OnPress() {
                @Override
                public void onPress() {
                    //通过密码获取助记词
                    //Get mnemonic by password
                    String[] word = AesUtils.aesDecrypt(App.getSpString(App.word)).split(",");
                    Arrays.asList(word);
                    //通过助记词和币种生成地址
                    //Generate address from mnemonic and currency
                    creatAddress2(word,type);
                }
            }).show();
        }
    }

    /**
     * 生成地址
     * generate address
     * @param word 助记词 mnemonic
     * @param type 币种 currency
     */

    private void creatAddress2(String []word,int type){
        //通过助记词获取dh对象
        //Get dh object by mnemonic
        DeterministicHierarchy dh = SeedMasterKey.seedMasterKey( Arrays.asList(word));
        //获取已保存的地址列表
        //Get a list of saved addresses
        List<CoinSetBean> list=App.getAddressList();
        //初始化币种实体类
        //Initialize the currency entity class
        CoinSetBean bean=new CoinSetBean();
        bean.setType(type);
        bean.setCheck(true);
        switch (type){
            case 0:
                //生成btc地址
                //Generate btc address
                AddressDTO btcAddress = Bitcoin.getInstance().address(dh,0);
                bean.setAddress(btcAddress.getAddress());
                bean.setName("BTC");
                break;
            case 1:
                //生成eth地址
                //Generate eth address
                Ethereum eth = Ethereum.getInstance();
                AddressDTO addressDTO2 = eth.address(dh, 0);
                bean.setAddress(addressDTO2.getAddress());
                bean.setName("ETH");
                break;


        }


        assert list != null;

        list.add(bean);
        //把新生成的地址保存到app存储中
        //Save the newly generated address to app storage
        App.saveAddressList(list);
        //设置选中状态
        //set selected state
        this.list.get(type).setCheck(true);
        //更新UI
        //Update UI
        coinSetAdapter.notifyDataSetChanged();
        showToast(getStringResources(R.string.creat_address_success));

    }
}
