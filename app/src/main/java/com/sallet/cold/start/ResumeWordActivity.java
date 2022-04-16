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
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Check backed up mnemonics
 */
public class ResumeWordActivity extends BaseActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.gv_1)
    GridView gv1;
    @BindView(R.id.gv_2)
    GridView gv2;
    @BindView(R.id.bt)
    TextView bt;

    String[] word; // out-of-order mnemonic
    String[] wordSes;// mnemonics in correct order
    List<Integer> positionArr = new ArrayList<>();//保存输入的位置 Save the entered location
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;

    private MyAdapter sim_adapter;//
    private MyAdapter1 sim_adapter1;//
    private Random random = new Random();
    private List<Map<String, Object>> data_list;// Out of order collection
    private List<Map<String, Object>> data_list1;// set of inputs
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_word);
        ButterKnife.bind(this);
        // out-of-order mnemonic
        word = AesUtils.aesDecrypt(getIntent().getStringExtra(App.password),getIntent().getStringExtra("words")).split(",");
        // mnemonics in correct order
        wordSes = AesUtils.aesDecrypt(getIntent().getStringExtra(App.password),getIntent().getStringExtra("words")).split(",");
        //scramble the mnemonic
        changePosition();
        data_list = new ArrayList<Map<String, Object>>();
        data_list1 = new ArrayList<Map<String, Object>>();
        String[] from = {"value"};
        int[] to = {R.id.tv_content};
        //convert array to set
        getData();
        //Adapter to initialize out-of-order mnemonics
        sim_adapter = new MyAdapter(this, data_list, R.layout.item_word_show, from, to);
        //Adapter that initializes the filled-in mnemonic
        sim_adapter1 = new MyAdapter1(this, data_list1, R.layout.item_word_input, from, to);
        //Set up the adapter
        gv2.setAdapter(sim_adapter);
        gv1.setAdapter(sim_adapter1);
        //Button initialization is not clickable
        bt.setEnabled(false);
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
        //All verifications are successful to start the next page
        startActivity(new Intent(context, CreatOkActivity.class)
                .putExtra(App.password,getIntent().getStringExtra( App.password))
                .putExtra("words",getIntent().getStringExtra("words")));
    }

    /**
     * Shuffle the original mnemonic order
     */
    public void changePosition() {
        for (int index = word.length - 1; index >= 0; index--) {
            //Iterate over the collection to swap positions based on random numbers
            exchange(random.nextInt(index + 1), index);
        }

    }

    /**
     * swap places
     * @param p1  new location
     * @param p2  Original location
     */
    private void exchange(int p1, int p2) {
        String temp = word[p1];
        word[p1] = word[p2];
        word[p2] = temp;  //
    }

    /**
     * set data source
     */
    public void getData() {
        //iterate over the collection
        for (int i = 0; i < word.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("value", word[i]);
            //add to the collection
            data_list.add(map);
        }

    }

    /**
     * set input data
     */
    public void getData1() {
        data_list1.clear();
        for (int i = 0; i < positionArr.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            //value is the position saved in the positionArr collection
            map.put("value", word[positionArr.get(i)]);
            //add to the collection
            data_list1.add(map);
        }
    }

    /**
     * An adapter showing all mnemonics out of order
     */
    class MyAdapter extends SimpleAdapter {
        private Context context;

        /**
         *  Construction method
         * @param context  context
         * @param data  data source
         * @param resource  layout file
         * @param from  layout key
         * @param to  the value of the layout
         */
        public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);

            final TextView tv_content = convertView.findViewById(R.id.tv_content);
            //Set click listener for parent layout
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSave = false;// Has it been recorded
                    if (positionArr.size() > 0) {// If the selected array is not 0
                        for (int i = 0; i < positionArr.size(); i++) {
                            // iterate over the selected array
                            if (position == positionArr.get(i)) {
                                //If the position is equal to the value stored in the selected array, the position is selected
                                isSave = true;
                            }
                        }
                        if (!isSave) {
                            //If not selected, add the position to the array
                            positionArr.add(position);
                            //refresh data
                            getData1();
                            //refresh UI
                            sim_adapter1.notifyDataSetChanged();
                            //Refresh the out-of-order collection list
                            data_list.add(new HashMap<String, Object>());
                            data_list.remove(data_list.size() - 1);
                            notifyDataSetChanged();
                        }
                    } else {
                        //If not selected, add the position to the array
                        positionArr.add(position);
                        //refresh data
                        getData1();
                        //refresh UI
                        sim_adapter1.notifyDataSetChanged();
                        //Refresh the out-of-order collection list
                        data_list.add(new HashMap<String, Object>());
                        data_list.remove(data_list.size() - 1);
                        notifyDataSetChanged();

                    }


                }
            });
            boolean isFalse = true;// Is the mnemonic order wrong?
            for (int i = 0; i < data_list1.size(); i++) {
                if (!wordSes[i].equals(data_list1.get(i).get("value"))) {
                    //Returns an error if the mnemonic in the correct order is not equal to the entered mnemonic
                    isFalse = false;
                }
            }
            if (!isFalse) {
                //If not normal UI prompt error
                title.setVisibility(View.VISIBLE);
                //button greyed out
                bt.setEnabled(false);
            } else {
                title.setVisibility(View.GONE);
                if(data_list1.size()==12) {
                    //The number of mnemonics is correct and the button can be clicked
                    bt.setEnabled(true);
                }else {
                    //Otherwise the button is greyed out
                    bt.setEnabled(false);
                }
            }
            boolean isSave = false;// whether to save
            for (int i = 0; i < positionArr.size(); i++) {
                if (position == positionArr.get(i)) {// Compare whether the location has been saved
                    isSave = true;
                }
            }
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
            //Click events for saved collections
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //remove the position from the collection
                    data_list1.remove(position);
                    //remove the element from the saved array
                    positionArr.remove(position);
                    //refresh UI
                    sim_adapter1.notifyDataSetChanged();
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
