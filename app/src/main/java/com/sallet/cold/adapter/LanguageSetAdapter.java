package com.sallet.cold.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sallet.cold.R;
import com.sallet.cold.bean.CoinSetBean;
import com.sallet.cold.bean.LanguageBean;

import java.util.List;

/**
 * 语言设置adapter
 * language setting adapter
 */
public class LanguageSetAdapter extends BaseQuickAdapter<LanguageBean, BaseViewHolder> {
    //构造器，传入数据和布局
    //Constructor, passing in data and layout
    public LanguageSetAdapter(int layoutResId, @Nullable List<LanguageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LanguageBean item) {
        //显示语言名称
        //Display language name
        helper.setText(R.id.tv_language,item.getName()+"");
        ImageView iv=helper.itemView.findViewById(R.id.iv_check);
        //该语种是否被选中
        //Whether the language is selected
        iv.setSelected(item.isCheck());
    }
}
