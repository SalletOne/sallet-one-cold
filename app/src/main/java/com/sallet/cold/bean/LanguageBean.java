package com.sallet.cold.bean;

/**
 *
 */
public class LanguageBean {
    private boolean check;// Whether the language is selected
    private String name;// language name
    private String code;// language code

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
