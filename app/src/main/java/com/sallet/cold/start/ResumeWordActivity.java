package com.sallet.cold.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sallet.cold.App;
import com.sallet.cold.R;
import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.utils.AesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 帮助用户校验助记词，分为上下两个列表，一个是打乱助记词顺序的列表，一个是空列表
 * 通过点击打乱顺序的列表条目，填入空列表，来校验用户是否正确记住助记词
 * Check backed up mnemonics
 */
public class ResumeWordActivity extends BaseActivity {


    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.gv_1)
    GridView gv1;
    @InjectView(R.id.gv_2)
    GridView gv2;
    @InjectView(R.id.bt)
    TextView bt;

    String[] word; //打乱顺序的助记词 out-of-order mnemonic
    String[] wordSes;//正确顺序的助记词 mnemonics in correct order
    List<Integer> positionArr = new ArrayList<>();//保存输入的位置 Save the entered location
    @InjectView(R.id.rl_back)
    RelativeLayout rlBack;

    private MyAdapter sim_adapter;//乱序的adapter
    private MyAdapter1 sim_adapter1;//输入的adapter
    private Random random = new Random();
    private List<Map<String, Object>> data_list;//乱序的集合 Out of order collection
    private List<Map<String, Object>> data_list1;//输入的集合 set of inputs
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_word);
        ButterKnife.inject(this);
        //打乱顺序的助记词 out-of-order mnemonic
        word = AesUtils.aesDecrypt(getIntent().getStringExtra("words")).split(",");
        //正确顺序的助记词 mnemonics in correct order
        wordSes = AesUtils.aesDecrypt(getIntent().getStringExtra("words")).split(",");
        //打乱助记词
        //scramble the mnemonic
        changePosition();
        data_list = new ArrayList<Map<String, Object>>();
        data_list1 = new ArrayList<Map<String, Object>>();
        String[] from = {"value"};
        int[] to = {R.id.tv_content};
        //数组转成集合
        //convert array to set
        getData();
        //初始化乱序的助记词的适配器
        //Adapter to initialize out-of-order mnemonics
        sim_adapter = new MyAdapter(this, data_list, R.layout.item_word_show, from, to);
        //初始化填入的助记词的适配器
        //Adapter that initializes the filled-in mnemonic
        sim_adapter1 = new MyAdapter1(this, data_list1, R.layout.item_word_input, from, to);
        //设置适配器
        //Set up the adapter
        gv2.setAdapter(sim_adapter);
        gv1.setAdapter(sim_adapter1);
        //按钮初始化不可点击
        //Button initialization is not clickable
        bt.setEnabled(false);
        //返回键监听
        //return key listener
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.bt)
    public void onClick() {
        //全部校验成功启动下一页
        //All verifications are successful to start the next page
        startActivity(new Intent(context, CreatOkActivity.class).putExtra("words",getIntent().getStringExtra("words")));
    }

    /**
     * 打乱数组顺序的方法
     * Shuffle the original mnemonic order
     */
    public void changePosition() {
        for (int index = word.length - 1; index >= 0; index--) {
            //遍历集合根据随机数交换位置
            //Iterate over the collection to swap positions based on random numbers
            exchange(random.nextInt(index + 1), index);
        }

    }

    /**
     * 交换位置
     * swap places
     * @param p1 新位置 new location
     * @param p2 原位置 Original location
     */
    private void exchange(int p1, int p2) {
        String temp = word[p1];
        word[p1] = word[p2];
        word[p2] = temp;  //
    }

    /**
     * 设置打乱的数据
     * set data source
     */
    public void getData() {
        //遍历集合
        //iterate over the collection
        for (int i = 0; i < word.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("value", word[i]);
            //加入到集合中
            //add to the collection
            data_list.add(map);
        }

    }

    /**
     * 设置输入的数据
     * set input data
     */
    public void getData1() {
        data_list1.clear();
        for (int i = 0; i < positionArr.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            //value是positionArr集合中保存的位置
            //value is the position saved in the positionArr collection
            map.put("value", word[positionArr.get(i)]);
            //加入到集合中
            //add to the collection
            data_list1.add(map);
        }
    }

    /**
     * 展示所有乱序助记词的adapter
     * An adapter showing all mnemonics out of order
     */
    class MyAdapter extends SimpleAdapter {
        private Context context;

        /**
         * 构造方法 Construction method
         * @param context 上下文 context
         * @param data 数据源 data source
         * @param resource 布局文件 layout file
         * @param from 布局的key layout key
         * @param to 布局的value the value of the layout
         */
        public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);

            final TextView tv_content = convertView.findViewById(R.id.tv_content);
            //给父布局设置点击监听
            //Set click listener for parent layout
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSave = false;//是否被选中 Has it been recorded
                    if (positionArr.size() > 0) {//如果选中的数组不为0 If the selected array is not 0
                        for (int i = 0; i < positionArr.size(); i++) {
                            //遍历选中数组 iterate over the selected array
                            if (position == positionArr.get(i)) {
                                //位置和选中数组中保存的值想等，则该位置为被选中过
                                //If the position is equal to the value stored in the selected array, the position is selected
                                isSave = true;
                            }
                        }
                        if (!isSave) {
                            //如果未选中过则将该位置加入数组中
                            //If not selected, add the position to the array
                            positionArr.add(position);
                            //刷新数据
                            //refresh data
                            getData1();
                            //刷新UI
                            //refresh UI
                            sim_adapter1.notifyDataSetChanged();
                            //刷新乱序的集合列表
                            //Refresh the out-of-order collection list
                            data_list.add(new HashMap<String, Object>());
                            data_list.remove(data_list.size() - 1);
                            notifyDataSetChanged();
                        }
                    } else {
                        //如果未选中过则将该位置加入数组中
                        //If not selected, add the position to the array
                        positionArr.add(position);
                        //刷新数据
                        //refresh data
                        getData1();
                        //刷新UI
                        //refresh UI
                        sim_adapter1.notifyDataSetChanged();
                        //刷新乱序的集合列表
                        //Refresh the out-of-order collection list
                        data_list.add(new HashMap<String, Object>());
                        data_list.remove(data_list.size() - 1);
                        notifyDataSetChanged();

                    }


                }
            });
            boolean isFalse = true;//判断已选助记词的顺序是否错误 Is the mnemonic order wrong?
            for (int i = 0; i < data_list1.size(); i++) {
                if (!wordSes[i].equals(data_list1.get(i).get("value"))) {
                    //如果正确顺序的助记词不等于输入的助记词则返回错误
                    //Returns an error if the mnemonic in the correct order is not equal to the entered mnemonic
                    isFalse = false;
                }
            }
            if (!isFalse) {
                //如果不正常UI提示错误
                //If not normal UI prompt error
                title.setVisibility(View.VISIBLE);
                //按钮置灰
                //button greyed out
                bt.setEnabled(false);
            } else {
                title.setVisibility(View.GONE);
                if(data_list1.size()==12) {
                    //助记词个数正确按钮可以点击
                    //The number of mnemonics is correct and the button can be clicked
                    bt.setEnabled(true);
                }else {
                    //否则按钮置灰
                    //Otherwise the button is greyed out
                    bt.setEnabled(false);
                }
            }
            boolean isSave = false;//是否保存 whether to save
            for (int i = 0; i < positionArr.size(); i++) {
                if (position == positionArr.get(i)) {//比对是否保存过该位置 Compare whether the location has been saved
                    isSave = true;
                }
            }
            //更改保存状态
            //Change save state
            if (!isSave) {
                tv_content.setTextColor(getResources().getColor(R.color.color_black_333));
            } else {
                tv_content.setTextColor(getResources().getColor(R.color.color_ac));
            }


            return convertView;
        }


    }

    /**
     * 展示输入数据的adapter
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
            //已保存集合的点击事件
            //Click events for saved collections
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //集合中移除该位置
                    //remove the position from the collection
                    data_list1.remove(position);
                    //保存的数组中移除该元素
                    //remove the element from the saved array
                    positionArr.remove(position);
                    //刷新UI
                    //refresh UI
                    sim_adapter1.notifyDataSetChanged();
                    //刷新乱序集合的UI
                    //Refresh UI for out-of-order collections
                    data_list.add(new HashMap<String, Object>());
                    data_list.remove(data_list.size() - 1);
                    sim_adapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }


    }
}
