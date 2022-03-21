package com.sallet.cold.start;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sallet.cold.App;
import com.sallet.cold.R;
import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.dialog.SureDialog;
import com.sallet.cold.utils.AesUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 备份助记词，页面展示12个有顺序的助记词，用户用来备份
 * Backup mnemonics, the page displays 12 sequential mnemonics, which are used by users to backup
 */

public class BackUpWordActivity extends BaseActivity {


    @InjectView(R.id.rl_back)
    RelativeLayout rlBack;

    @InjectView(R.id.g_view)
    GridView gView;
    @InjectView(R.id.bt)
    TextView bt;
    private List<Map<String, Object>> data_list;//助记词集合 mnemonic collection
    private SimpleAdapter sim_adapter;//助记词适配器 Mnemonic Adapter

    private String[] value;//助记词内容 mnemonic content
    private String[] num = { "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "11", "12" };
    int root;//1 是从首页进来的，其他是从起始页进来的 1 is from the home page, others are from the start page

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up_word);
        ButterKnife.inject(this);
        //新建集合
        //New collection
        data_list = new ArrayList<Map<String, Object>>();
        root=getIntent().getIntExtra("root",0);
        if(root==0) {
            //root==0 从起始页进来需要解密助记词展示
            //root==0 Entering from the start page needs to decrypt the mnemonic display
            value = AesUtils.aesDecrypt(getIntent().getStringExtra("words")).split(",");
        }else {
            //从主页进来助记词已经是解密过带过来的
            //The mnemonic words that came in from the homepage have already been decrypted and brought over
            value=getIntent().getStringArrayExtra("words");
        }
        //格式化助记词把数组转成集合
        //Formatting mnemonics to convert arrays into sets
        getData();
        //初始化适配器的key
        //key to initialize the adapter
        String [] from ={"value","num"};
        //初始化适配器的布局
        //Initialize the layout of the adapter
        int [] to = {R.id.tv_content,R.id.tv_num};
        sim_adapter = new SimpleAdapter(context, data_list, R.layout.item_word_creat, from, to);
        //展示UI
        //Show UI
        gView.setAdapter(sim_adapter);


    }

    @OnClick({R.id.rl_back, R.id.bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.bt:
                if(root==1){
                    //从主页进来的直接结束就可以
                    //You can end it directly from the home page
                    finish();
                }else {
                    //确认弹窗用于进入下个页面
                    //Confirm the pop-up window is used to enter the next page
                    new SureDialog(context, getIntent().getStringExtra("words")).show();
                }
                break;
        }
    }



    public List<Map<String, Object>> getData(){
        //初始化数据把数组转为集合
        //Initialization data
        for(int i=0;i<value.length;i++){
            //新建map
            //new map
            Map<String, Object> map = new HashMap<String, Object>();
            //设置map中的key value
            //Set the key value in the map
            map.put("value", value[i]);
            map.put("num", num[i]);
            //add to the collection
            data_list.add(map);
        }

        return data_list;
    }



}
