package com.sallet.cold.bean;

/**
 * Entity class of self selected mnemonics
 */
public class WordCheckBean {
    private String words; //Mnemonic words
    private boolean check;//Checked

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
