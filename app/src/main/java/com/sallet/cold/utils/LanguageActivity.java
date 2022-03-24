package com.sallet.cold.utils;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sallet.cold.App;
import com.sallet.cold.R;
import com.sallet.cold.adapter.LanguageSetAdapter;
import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.bean.LanguageBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Select the software language,
 * after selection, the software default language is the selected language, which is saved in the APP storage
 */
public class LanguageActivity extends BaseActivity {


    @InjectView(R.id.rl_back)
    RelativeLayout rlBack;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    @InjectView(R.id.rc_view)
    RecyclerView rcView;
    //Language list adapter
    LanguageSetAdapter adapter;
    //List of supported languages
    List<LanguageBean> list=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.inject(this);
        rcView.setLayoutManager(new LinearLayoutManager(context));
        //Initialize the adapter
        adapter=new LanguageSetAdapter(R.layout.item_language,list);
        rcView.setAdapter(adapter);
        //Add click event to list
        adapter.setOnItemClickListener((adapter, view, position) -> {
            for(int i=0;i<list.size();i++){
                //Traverse the collection, the selected check is set to true, the others are set to false
                list.get(i).setCheck(position == i);
            }
            //refresh data
            this.adapter.notifyDataSetChanged();
            //change language approach
            changeLanguage(list.get(position).getCode());
        });
        //Initialize the list of supported languages
        initData();

    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    private void initData(){
        for (int i=0;i<25;i++) {
            LanguageBean bean = new LanguageBean();
            switch (i){
                case 0:
                    bean.setName("English\n"+getStringResources(R.string.lg_English));
                    bean.setCode("en");
                    break;
                case 1:
                    bean.setName("中文\n"+getStringResources(R.string.lg_chinese));
                    bean.setCode("zh");
                    break;

                case 2:
                    bean.setName("កម្ពុជា។\n"+getStringResources(R.string.lg_Cambodian));
                    bean.setCode("kh");
                    break;
                case 3:
                    bean.setName("ພາສາລາວ\n"+getStringResources(R.string.lg_Lao));
                    bean.setCode("lo");
                    break;
                case 4:
                    bean.setName("Melayu\n"+getStringResources(R.string.lg_Malay));
                    bean.setCode("ms");
                    break;
                case 5:
                    bean.setName("Indonesian\n"+getStringResources(R.string.lg_Indonesian));
                    bean.setCode("in");
                    break;
                case 6:
                    bean.setName("Tiếng Việt\n"+getStringResources(R.string.lg_Vietnamese));
                    bean.setCode("vi");
                    break;
                case 7:
                    bean.setName("Arabic\n"+getStringResources(R.string.lg_Arabic));
                    bean.setCode("ar");
                    break;
                case 8:
                    bean.setName("français\n"+getStringResources(R.string.lg_French));
                    bean.setCode("fr");
                    break;
                case 9:
                    bean.setName("हिंदी\n"+getStringResources(R.string.lg_Hindi));
                    bean.setCode("hi");
                    break;
                case 10:
                    bean.setName("Italiano\n"+getStringResources(R.string.lg_Italian));
                    bean.setCode("it");
                    break;
                case 11:
                    bean.setName("русский\n"+getStringResources(R.string.lg_Russian));
                    bean.setCode("ru");
                    break;
                case 12:
                    bean.setName("한국인\n"+getStringResources(R.string.lg_Korean));
                    bean.setCode("ko");
                    break;
                case 13:
                    bean.setName("မြန်မာ\n"+getStringResources(R.string.lg_Burmese));
                    bean.setCode("my");
                    break;
                case 14:
                    bean.setName("日本\n"+getStringResources(R.string.lg_Japanese));
                    bean.setCode("ja");
                    break;
                case 15:
                    bean.setName("ไทย\n"+getStringResources(R.string.lg_Thai));
                    bean.setCode("th");
                    break;
                case 16:
                    bean.setName("Español\n"+getStringResources(R.string.lg_Spanish));
                    bean.setCode("es");
                    break;
                case 17:
                    bean.setName("Suomalainen\n"+getStringResources(R.string.lg_Finnish));
                    bean.setCode("fi");
                    break;
                case 18:
                    bean.setName("português\n"+getStringResources(R.string.lg_Portuguese));
                    bean.setCode("pt");
                    break;
                case 19:
                    bean.setName("svenska\n"+getStringResources(R.string.lg_Swedish));
                    bean.setCode("sv");
                    break;
                case 20:
                    bean.setName("Persian\n"+getStringResources(R.string.lg_Persian));
                    bean.setCode("sr");
                    break;
                case 21:
                    bean.setName("dansk\n"+getStringResources(R.string.lg_Danish));
                    bean.setCode("da");
                    break;
                case 22:
                    bean.setName("Deutsch\n"+getStringResources(R.string.lg_German));
                    bean.setCode("de");
                    break;
                case 23:
                    bean.setName("norsk\n"+getStringResources(R.string.lg_Norwegian));
                    bean.setCode("no");
                    break;
                case 24:
                    bean.setName("Ελληνικά\n"+getStringResources(R.string.lg_Greek));
                    bean.setCode("el");
                    break;
            }
            if(bean.getCode().equals(App.getSpString(App.language))){
                //Set the language stored in the APP to the selected state
                bean.setCheck(true);
            }
            list.add(bean);
        }


        adapter.notifyDataSetChanged();

    }


}
