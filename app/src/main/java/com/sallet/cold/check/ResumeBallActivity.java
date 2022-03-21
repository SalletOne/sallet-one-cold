package com.sallet.cold.check;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sallet.cold.App;
import com.sallet.cold.R;
import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.bean.Word;
import com.sallet.cold.start.CreatMoneyPassActivity;
import com.sallet.cold.utils.AesUtils;
import com.sallet.cold.utils.CommaTokenizer;
import com.sallet.cold.utils.RoundBackgroundColorSpan;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 恢复已存在的钱包，通过输入12个助记词来恢复曾经创建过的钱包
 *
 *Restore existing wallets, restore previously created wallets by entering 12 mnemonic phrases
 */
public class ResumeBallActivity extends BaseActivity {

    /**
     * 绑定UI
     * Bind UI
     */

    @InjectView(R.id.rl_back)
    RelativeLayout rlBack;

    @InjectView(R.id.et_pass)
    MultiAutoCompleteTextView etPass;
    @InjectView(R.id.bt)
    TextView bt;
    @InjectView(R.id.title)
    TextView title;


    String [] word;//助记词词库 mnemonic thesaurus
    String [] wordList;//输入的助记词数组 array of input mnemonics
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_ball);
        ButterKnife.inject(this);
        //从Word类中获取所有合法的助记词
        //Get all legal mnemonics from Word class
        word=new Word().getWords();
        //输入框输入规则
        //input box input rules
        String regular = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";
        etPass.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }

            @Override
            protected char[] getAcceptedChars() {
                //只允许输入制定好的字符串
                //Only allow input of the specified string
                return regular.toCharArray();
            }
        });
        //添加输入监听
        //add input listener
        etPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                //通过空格符分割输入的内容，分割为数组中的单个元素
                //Split the input by a space character into individual elements in the array
                wordList =s.toString().split(" ");
                //是否匹配词库中的单词
                //whether to match words in the thesaurus
                boolean isFalse=false;
                //遍历数组
                //iterate over the array
                for (String value : wordList) {
                    isFalse = false;
                    //遍历数组比对词库
                    //Traverse the array to compare thesaurus
                    for (String item : word) {
                        if (item.equals(value)) {
                            //词库中存在返回true
                            //Returns true if it exists in the thesaurus
                            isFalse = true;
                        }
                    }
                }

                if(!isFalse){
                    //输入了词库中不存在词汇按钮置灰禁止进行下一步
                    //Enter the vocabulary that does not exist in the vocabulary,
                    // the button is grayed out and prohibited from proceeding to the next step
                    bt.setEnabled(false);
                }else {
                    //校验输入的词汇是否是12个,是12个则可以进行下一步,否则按钮置灰禁用
                    //Check whether the entered vocabulary is 12, if it is 12, you can go to the next step,
                    // otherwise the button is grayed out and disabled
                        bt.setEnabled(wordList.length == 12);
                        if(wordList.length == 12) {
                            //长度正确清除输入
                            //Correct length clear input
                            clearInput();
                        }
                }
            }
        });

        //弹出提示词汇的adatper
        //The adapter that pops up the prompt word
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, R.layout.item_ling, new Word().getWords());
        etPass.setAdapter(adapter);
        //为MultiAutoCompleteTextView设置分隔符
        //Set separator for MultiAutoCompleteTextView
        etPass.setTokenizer(new CommaTokenizer());
    }

    @OnClick({R.id.rl_back, R.id.bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.bt:
                //完成进入下一步页面创建钱包密码
                //Complete and enter the next page to create a wallet password
                startActivity(new Intent(context, CreatMoneyPassActivity.class).putExtra("type", 1).putExtra("wordList",wordList));
                break;
        }
    }


    /**
     * 隐藏键盘
     * hide keyboard
     */
    private void clearInput(){
    InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm!=null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                    0);
        }
    }


}
