package com.sallet.cold.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sallet.cold.R;
import com.sallet.cold.bean.CoinSetBean;

import java.util.List;

/**
 * Home token list adapter
 */
public class MainAdapter extends BaseQuickAdapter<CoinSetBean, BaseViewHolder> {

    public MainAdapter(int layoutResId, @Nullable List<CoinSetBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinSetBean item) {
        //Show token name
        helper.setText(R.id.tv_name,item.getName()+"");
        //show token address
        helper.setText(R.id.tv_address,item.getAddress()+"");
        int  image=0;
        //Display different background images according to different currencies
        switch (item.getType()){
            case 0:
                image=R.mipmap.ic_home_bit;
                break;
            case 1:
                image=R.mipmap.ic_home_eth;
                break;
            case 2:
                image=R.mipmap.ic_home_dog;
                break;
            case 3:
                image=R.mipmap.ic_home_bch;
                break;
            case 4:
                image=R.mipmap.ic_home_ltc;
                break;
            case 5:
                image=R.mipmap.ic_home_fil;
                break;
            case 6:
                image=R.mipmap.ic_home_matic;
                break;
        }
        helper.setBackgroundRes(R.id.ll_content,image);

    }
}
