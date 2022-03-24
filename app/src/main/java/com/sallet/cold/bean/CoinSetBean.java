package com.sallet.cold.bean;

/**
 * Entity class for token information
 * Contains all elements of hardware that should showcase each token
 */
public class CoinSetBean implements Comparable<CoinSetBean>{
    private String name;// Token public chain name
    private String nameSign;// token name
    private String address;// Token address
    private int type; //  Token Class 0 btc 1eth  2doge 3bch 4ltc 5fil 6matic 7xrp 8sol 9dot 10trx
    private int  image;// Token picture
    private boolean  check=false;// Whether the token is displayed on the homepage

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
