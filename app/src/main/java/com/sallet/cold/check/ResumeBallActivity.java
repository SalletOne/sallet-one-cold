package com.sallet.cold.check;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
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

import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;


/**
 *
 *Restore existing wallets, restore previously created wallets by entering 12 mnemonic phrases
 */
public class ResumeBallActivity extends BaseActivity {

    /**
     * Bind UI
     */

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;//back

    @BindView(R.id.et_pass)
    MultiAutoCompleteTextView etPass;//Enter password box
    @BindView(R.id.bt)
    TextView bt; //Submit button
    @BindView(R.id.title)
    TextView title;


    String [] word;// mnemonic thesaurus
    String [] wordList;// array of input mnemonics
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_ball);
        ButterKnife.bind(this);
        //Get all legal mnemonics from Word class
        word=new Word().getWords();
        //input box input rules
        String regular = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";
        etPass.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }

            @Override
            protected char[] getAcceptedChars() {
                //Only allow input of the specified string
                return regular.toCharArray();
            }
        });

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
                //Split the input by a space character into individual elements in the array
                wordList =s.toString().split("\\s+");
                //whether to match words in the thesaurus
                boolean isFalse=false;
                //iterate over the array
                for (String value : wordList) {
                    isFalse = false;
                    //Traverse the array to compare thesaurus
                    for (String item : word) {
                        if (item.equals(value)) {
                            //Returns true if it exists in the thesaurus
                            isFalse = true;
                        }
                    }
                }

                if(!isFalse){
                    //Enter the vocabulary that does not exist in the vocabulary,
                    // the button is grayed out and prohibited from proceeding to the next step
                    bt.setEnabled(false);
                }else {
                    //Check whether the entered vocabulary is 12, if it is 12, you can go to the next step,
                    // otherwise the button is grayed out and disabled
                        bt.setEnabled(wordList.length == 12);
                        if(wordList.length == 12) {
                            //Correct length clear input
                            clearInput();
                        }
                }
            }
        });

        //The adapter that pops up the prompt word
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, R.layout.item_ling, new Word().getWords());
        etPass.setAdapter(adapter);
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


                for (String value : wordList) {
                    boolean isOK=false;
                    //Traverse the array to compare thesaurus
                    for (String item : word) {
                        if (item.equals(value)) {
                            //Returns true if it exists in the thesaurus
                            isOK=true;
                        }
                    }
                    if(!isOK) {
                        showToast(value + " " + getString(R.string.words_not_yes));
                        return;
                    }

                    try {
                        MnemonicCode.INSTANCE.check(Arrays.asList(wordList));
                    } catch (MnemonicException e) {
                        showToast(getString(R.string.illegal_mnemonic));
                        return;
                    }

                }


                //Complete and enter the next page to create a wallet password
                startActivity(new Intent(context, CreatMoneyPassActivity.class).putExtra("type", 1).putExtra("wordList",wordList));
                break;
        }
    }


    /**
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
