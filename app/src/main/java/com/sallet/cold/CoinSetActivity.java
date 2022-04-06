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
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Currency address management
 */
public class CoinSetActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.rc_view)
    RecyclerView rcView;
    List<CoinSetBean> list=new ArrayList<>();// address list
    CoinSetAdapter coinSetAdapter;// address list adapter
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_set);
        ButterKnife.bind(this);
        //dividing line between each item
        rcView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.VERTICAL));

        coinSetAdapter=new CoinSetAdapter(R.layout.item_coin_set,list);
        rcView.setLayoutManager(new LinearLayoutManager(context));
        rcView.setAdapter(coinSetAdapter);
        //adapter add list click event
        coinSetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Is it selected
                boolean check=list.get(position).isCheck();
                //Get the address that has been generated and saved
                List<CoinSetBean>addressList=App.getAddressList();
                //whether the address has been generated
                boolean isHave=false;
                for(int i=0;i<addressList.size();i++){
                    CoinSetBean bean=addressList.get(i);
                    if(bean.getType()==position){
                        isHave=true;
                        //set open state
                        bean.setCheck(!check);
                        //Modify the open state of this location in the collection
                        addressList.set(i,bean);
                        //Save the updated collection
                        App.saveAddressList(addressList);
                    }
                }

                if(!isHave&&!check) {
                    //Not Generated If not selected, generate the address
                    creatAddress(position);
                }else {
                    //Generated direct change object state
                    list.get(position).setCheck(!check);

                }
                //Update UI
                coinSetAdapter.notifyDataSetChanged();
            }
        });
        //Initialization data
        initData();
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    /**
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

        data2.setName("Dogecoin");
        data2.setNameSign("DOGE");
        data2.setImage(R.mipmap.ic_dog_logo);
        list.add(data2);
        CoinSetBean data3=new CoinSetBean();
        data3.setName("Bitcoin Cash");
        data3.setNameSign("BCH");
        data3.setImage(R.mipmap.ic_set_bch);
        list.add(data3);
        CoinSetBean data4=new CoinSetBean();
        data4.setName("Litecoin");
        data4.setNameSign("LTC");
        data4.setImage(R.mipmap.ic_set_ltc);
        list.add(data4);
        CoinSetBean data5=new CoinSetBean();
        data5.setName("Filecoin");
        data5.setNameSign("FIL");
        data5.setImage(R.mipmap.ic_set_fil);
        list.add(data5);
        List<CoinSetBean>addressList=App.getAddressList();
        //Initialize saved data
        for(int i=0;i<addressList.size();i++){ //
                CoinSetBean bean=list.get(addressList.get(i).getType());
                bean.setCheck(addressList.get(i).isCheck());
                list.set(addressList.get(i).getType(),bean);
        }




        coinSetAdapter.notifyDataSetChanged();
    }


    /**
     * Generate address by mnemonic generation
     * @param type 币种
     */


    private void creatAddress(int type){
        //If the global saved password is not empty, the address will be generated directly
        if(App.passWord.length()>0) {
            //Get mnemonic by password
            String[] word = AesUtils.aesDecrypt(App.getSpString(App.word)).split(",");
            Arrays.asList(word);
            //Generate address from mnemonic and currency
            creatAddress2(word,type);
            //set checked
            list.get(type).setCheck(true);
            //Update UI state
            coinSetAdapter.notifyDataSetChanged();
        }else {
            //If the global save password is empty, the address can only be generated after entering the correct password.
            new PwDialog(context, "",  getStringResources(R.string.please_input_etpass), new PwDialog.OnPress() {
                @Override
                public void onPress() {
                    //Get mnemonic by password
                    String[] word = AesUtils.aesDecrypt(App.getSpString(App.word)).split(",");
                    Arrays.asList(word);
                    //Generate address from mnemonic and currency
                    creatAddress2(word,type);
                }
            }).show();
        }
    }

    /**
     * generate address
     * @param word  mnemonic
     * @param type  currency
     */

    private void creatAddress2(String []word,int type){
        //Get dh object by mnemonic
        DeterministicHierarchy dh = SeedMasterKey.seedMasterKey( Arrays.asList(word));
        //Get a list of saved addresses
        List<CoinSetBean> list=App.getAddressList();
        //Initialize the currency entity class
        CoinSetBean bean=new CoinSetBean();
        bean.setType(type);
        bean.setCheck(true);
        switch (type){
            case 0:
                //Generate btc address
                AddressDTO btcAddress = Bitcoin.getInstance().address(dh,0);
                bean.setAddress(btcAddress.getAddress());
                bean.setName("BTC");
                break;
            case 1:
                //Generate eth address
                Ethereum eth = Ethereum.getInstance();
                AddressDTO addressDTO2 = eth.address(dh, 0);
                bean.setAddress(addressDTO2.getAddress());
                bean.setName("ETH");
                break;
            case 2:
                //Generate doge address
                AddressDTO addressDTO3 = Dogecoin.getInstance().address(dh, 0);
                bean.setAddress(addressDTO3.getAddress());
                bean.setName("DOGE");
                break;
            case 3:
                //Generate bch address
                AddressDTO addressDTO5 = BitcoinCash.getInstance().address(dh, 0);
                bean.setAddress(addressDTO5.getAddress());
                bean.setName("BCH");
                break;
            case 4:
                //Generate ltc address
                AddressDTO addressDTO4 = Litecoin.getInstance().address(dh, 0);
                bean.setAddress(addressDTO4.getAddress());
                bean.setName("LTC");

                break;
            case 5:
                //Generate fil address
                AddressDTO addressDTO6 = Filecoin.getInstance().address(dh, 0);
                bean.setAddress(addressDTO6.getAddress());
                bean.setName("FIL");
        }


        assert list != null;

        list.add(bean);
        //Save the newly generated address to app storage
        App.saveAddressList(list);
        //set selected state
        this.list.get(type).setCheck(true);
        //Update UI
        coinSetAdapter.notifyDataSetChanged();
        showToast(getStringResources(R.string.creat_address_success));

    }
}
