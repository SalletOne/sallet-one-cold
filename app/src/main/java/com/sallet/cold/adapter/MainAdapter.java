package com.sallet.cold.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sallet.cold.R;
import com.sallet.cold.bean.CoinSetBean;

import java.util.List;

/**
 * 主页代币列表adapter
 * Home token list adapter
 */
public class MainAdapter extends BaseQuickAdapter<CoinSetBean, BaseViewHolder> {

    public MainAdapter(int layoutResId, @Nullable List<CoinSetBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinSetBean item) {
        //显示代币名称
        //Show token name
        helper.setText(R.id.tv_name,item.getName()+"");
        //显示代币地址
        //show token address
        helper.setText(R.id.tv_address,item.getAddress()+"");
        int  image=0;
        //根据币种不同展示不同的背景图
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
        }
        helper.setBackgroundRes(R.id.ll_content,image);

    }
}
