package com.sallet.cold.start;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sallet.cold.App;
import com.sallet.cold.MainActivity;
import com.sallet.cold.R;
import com.sallet.cold.adapter.ItemAdapter;
import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.bean.Word;
import com.sallet.cold.bean.WordCheckBean;
import com.sallet.cold.utils.AesUtils;
import com.sallet.cold.utils.azlist.AZItemEntity;
import com.sallet.cold.utils.azlist.AZSideBarView;
import com.sallet.cold.utils.azlist.AZTitleDecoration;
import com.sallet.cold.utils.azlist.LettersComparator;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CenterHintActivity extends BaseActivity {

    private TextView tvCancle;

    public static void startUp(Context context) {
        context.startActivity(new Intent(context, CenterHintActivity.class));
    }

    private MyAdapter1 sim_adapter1;//
    private Context mContext;
    private RecyclerView mRecyclerView;
    private AZSideBarView mBarList;
    private ItemAdapter mAdapter;
    private GridView gv_2;
    private EditText search;
    private TextView back;
    private List<Map<String, Object>> data_list1;// set of inputs
    List<AZItemEntity<WordCheckBean>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_center_hit);
        ButterKnife.bind(this);
        initView();
        initEvent();
        initData();
        data_list1 = new ArrayList<Map<String, Object>>();
        String[] from = {"value"};
        int[] to = {R.id.tv_content};
        sim_adapter1 = new MyAdapter1(this, data_list1, R.layout.item_word_input, from, to);
        gv_2.setAdapter(sim_adapter1);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.getFilter().filter(s.toString());
            }
        });
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recycler_list);
        gv_2 = findViewById(R.id.gv_2);
        back = findViewById(R.id.tv_back);
        back.setOnClickListener(v -> finish());
        search = findViewById(R.id.search);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new AZTitleDecoration(new AZTitleDecoration.TitleAttributes(mContext)));
        mBarList = findViewById(R.id.bar_list);
    }

    private void initEvent() {
        mBarList.setOnLetterChangeListener(new AZSideBarView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int position = mAdapter.getSortLettersFirstPosition(letter);
                if (position != -1) {
                    if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                        manager.scrollToPositionWithOffset(position, 0);
                    } else {
                        mRecyclerView.getLayoutManager().scrollToPosition(position);
                    }
                }
            }
        });
    }

    private void initData() {
        data = new ArrayList<>();
        mAdapter = new ItemAdapter(data, position -> {


            if (data_list1.size() < 12 && !data.get(position).getValue().isCheck()) {

                //Traverse the collection, the selected check is set to true, the others are set to false
                data.get(position).getValue().setCheck(true);
                //refresh data
                mAdapter.notifyDataSetChanged();
                putAddData(position);
            } else if (data.get(position).getValue().isCheck()) {
                //Traverse the collection, the selected check is set to true, the others are set to false
                data.get(position).getValue().setCheck(false);
                //refresh data
                mAdapter.notifyDataSetChanged();
                removeData(position);
            }
        }, () -> data=mAdapter.getDataList());
        mRecyclerView.setAdapter(mAdapter);
        List<AZItemEntity<String>> dateList = fillData(new Word().pass);
        Collections.sort(dateList, new LettersComparator());
        for (AZItemEntity<String> bean : dateList) {
            AZItemEntity<WordCheckBean> beanAZItemEntity = new AZItemEntity<>();
            WordCheckBean bean1 = new WordCheckBean();
            bean1.setWords(bean.getValue());
            beanAZItemEntity.setValue(bean1);
            beanAZItemEntity.setSortLetters(bean.getSortLetters());
            data.add(beanAZItemEntity);
        }
        mAdapter.notifyDataSetChanged();


    }

    private List<AZItemEntity<String>> fillData(String[] date) {
        List<AZItemEntity<String>> sortList = new ArrayList<>();
        for (String aDate : date) {
            AZItemEntity<String> item = new AZItemEntity<>();
            item.setValue(aDate);
            String pinyin = aDate.substring(0, 1);
            String letters = pinyin.substring(0, 1).toUpperCase();
            if (letters.matches("[A-Z]")) {
                item.setSortLetters(letters.toUpperCase());
            } else {
                item.setSortLetters("#");
            }
            sortList.add(item);
        }
        return sortList;

    }

    @OnClick({R.id.tv_back, R.id.tv_put, R.id.tv_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
            	finish();
                break;
            case R.id.tv_put:
            	if(data_list1.size()==12) {
            		String []value = new String[12];
            		for(int i=0;i<data_list1.size();i++){
            			value[i]= (String) data_list1.get(i).get("value");
					}

                    startActivity(new Intent(context,BackUpWordActivity.class)
                            .putExtra( App.password,getIntent().getStringExtra(App.password))
                            .putExtra("words",AesUtils.aesEncrypt(getIntent().getStringExtra( App.password),StringUtils.join(value, ","))));

				}
                break;
            case R.id.tv_cancle:
				search.setText("");
                hideKeyboard();
                break;
        }
    }


    /**
     * An adapter that displays the input data
     */

    class MyAdapter1 extends SimpleAdapter {
        private Context context;

        public MyAdapter1(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            TextView tvNum = convertView.findViewById(R.id.tv_num);
            tvNum.setText((position + 1) + "");
            //Click events for saved collections
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    changeData(position);
                    //remove the position from the collection
                    data_list1.remove(position);

                    sim_adapter1.notifyDataSetChanged();
                }
            });
            return convertView;
        }


    }


    private void putAddData(int position) {
        Map<String, Object> map = new HashMap<String, Object>();
        //value is the position saved in the positionArr collection
        map.put("value", data.get(position).getValue().getWords());
        //add to the collection
        data_list1.add(map);
        sim_adapter1.notifyDataSetChanged();
    }

    private void removeData(int position){
        //value is the position saved in the positionArr collection
        String word= data.get(position).getValue().getWords();
        //add to the collection
        for(int i=0;i<data_list1.size();i++) {
            if(data_list1.get(i).get("value").equals(word)) {
                data_list1.remove(i);
            }
        }
        sim_adapter1.notifyDataSetChanged();
    }



    private void changeData(int position) {
        String word = (String) data_list1.get(position).get("value");
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getValue().getWords().equals(word)) {
                data.get(i).getValue().setCheck(false);
            }
        }
        mAdapter.notifyDataSetChanged();

    }




}
