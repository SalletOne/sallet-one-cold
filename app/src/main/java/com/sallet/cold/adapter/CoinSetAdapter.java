package com.sallet.cold.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sallet.cold.R;
import com.sallet.cold.bean.CoinSetBean;

import java.util.List;

/**
 * 代币设置adapter
 * Token Settings Adapter
 */
public class CoinSetAdapter extends BaseQuickAdapter<CoinSetBean, BaseViewHolder> {

    //构造器，传入数据和布局
    //Constructor, passing in data and layout
    public CoinSetAdapter(int layoutResId, @Nullable List<CoinSetBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinSetBean item) {
        //显示代币公链
        //Show token public chain
        helper.setText(R.id.tv_name,item.getName()+"");
        //显示代币名称
        //Show token name
        helper.setText(R.id.tv_name_sign,item.getNameSign()+"");
        //显示代币图标
        //Show token icon
        helper.setBackgroundRes(R.id.iv_type,item.getImage());
        //是否在主页中显示该代币
        //Whether to show the token in the homepage
        if(item.isCheck()) {
            //true 显示
            //true show
            helper.setImageResource(R.id.iv_set, R.mipmap.ic_set_on);
        }else {
            //false 不显示
            //false do not display
            helper.setImageResource(R.id.iv_set, R.mipmap.ic_set_off);
        }
    }
}
