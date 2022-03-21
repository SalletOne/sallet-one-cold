package com.sallet.cold.bean;

/**
 * 代币信息的实体类
 * 包含硬件应展示每个代币的所有元素
 * Entity class for token information
 * Contains all elements of hardware that should showcase each token
 */
public class CoinSetBean implements Comparable<CoinSetBean>{
    private String name;//代币公链名 Token public chain name
    private String nameSign;//代币名 token name
    private String address;//代币地址 Token address
    private int type; //代币类别  Token Class 0 btc 1eth  2doge 3bch 4ltc 5fil 6matic 7xrp 8sol 9dot 10trx
    private int  image;//代币图片 Token picture
    private boolean  check=false;//代币是否在主页显示 Whether the token is displayed on the homepage

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public int compareTo(CoinSetBean o) {
        return this.type - o.getType();

    }

    public String getNameSign() {
        return nameSign;
    }

    public void setNameSign(String nameSign) {
        this.nameSign = nameSign;
    }
}
