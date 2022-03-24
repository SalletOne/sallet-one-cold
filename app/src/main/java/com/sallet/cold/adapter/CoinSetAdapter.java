package com.sallet.cold.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sallet.cold.R;
import com.sallet.cold.bean.CoinSetBean;

import java.util.List;

/**
 * Token Settings Adapter
 */
public class CoinSetAdapter extends BaseQuickAdapter<CoinSetBean, BaseViewHolder> {

    //Constructor, passing in data and layout
    public CoinSetAdapter(int layoutResId, @Nullable List<CoinSetBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinSetBean item) {
        //Show token public chain
        helper.setText(R.id.tv_name,item.getName()+"");
        //Show token name
        helper.setText(R.id.tv_name_sign,item.getNameSign()+"");
        //Show token icon
        helper.setBackgroundRes(R.id.iv_type,item.getImage());
        //Whether to show the token in the homepage
        if(item.isCheck()) {
            //true show
            helper.setImageResource(R.id.iv_set, R.mipmap.ic_set_on);
        }else {
            //false do not display
            helper.setImageResource(R.id.iv_set, R.mipmap.ic_set_off);
        }
    }
}
