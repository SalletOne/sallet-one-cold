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
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Backup mnemonics, the page displays 12 sequential mnemonics, which are used by users to backup
 */

public class BackUpWordActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;

    @BindView(R.id.g_view)
    GridView gView;
    @BindView(R.id.bt)
    TextView bt;
    private List<Map<String, Object>> data_list;// mnemonic collection
    private SimpleAdapter sim_adapter;// Mnemonic Adapter

    private String[] value;// mnemonic content
    private String[] num = { "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "11", "12" };
    int root;// 1 is from the home page, others are from the start page

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up_word);
        ButterKnife.bind(this);
        //New collection
        data_list = new ArrayList<Map<String, Object>>();
        root=getIntent().getIntExtra("root",0);
        if(root==0) {
            //root==0 Entering from the start page needs to decrypt the mnemonic display
            value = AesUtils.aesDecrypt(getIntent().getStringExtra(App.password),getIntent().getStringExtra("words")).split(",");
        }else {
            //The mnemonic words that came in from the homepage have already been decrypted and brought over
            value=getIntent().getStringArrayExtra("words");
        }
        //Formatting mnemonics to convert arrays into sets
        getData();
        //key to initialize the adapter
        String [] from ={"value","num"};
        //Initialize the layout of the adapter
        int [] to = {R.id.tv_content,R.id.tv_num};
        sim_adapter = new SimpleAdapter(context, data_list, R.layout.item_word_creat, from, to);
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
                    //You can end it directly from the home page
                    finish();
                }else {
                    //Confirm the pop-up window is used to enter the next page
                    new SureDialog(context, getIntent().getStringExtra("words"),getIntent().getStringExtra(App.password)).show();
                }
                break;
        }
    }



    public List<Map<String, Object>> getData(){
        //Initialization data
        for(int i=0;i<value.length;i++){
            //new map
            Map<String, Object> map = new HashMap<String, Object>();
            //Set the key value in the map
            map.put("value", value[i]);
            map.put("num", num[i]);
            //add to the collection
            data_list.add(map);
        }

        return data_list;
    }



}
