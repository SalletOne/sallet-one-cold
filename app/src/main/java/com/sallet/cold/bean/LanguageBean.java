package com.sallet.cold.bean;

/**
 * 语言设置实体类
 */
public class LanguageBean {
    private boolean check;//语言是否选中状态 Whether the language is selected
    private String name;//语言名称 language name
    private String code;//语言代号 language code

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
