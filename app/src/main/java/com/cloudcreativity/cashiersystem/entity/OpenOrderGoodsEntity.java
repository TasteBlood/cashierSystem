package com.cloudcreativity.cashiersystem.entity;

import com.cloudcreativity.cashiersystem.utils.StrUtils;

/**
 * 开单商品
 */
public class OpenOrderGoodsEntity {

    private long gid;
    private String name;
    private String unit;
    private int price;
    private float number;
    private float discount;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String formatPrice(){
        return "￥"+ StrUtils.get2BitDecimal(this.price/100f);
    }

    public String formatTotal(){
        return "￥"+StrUtils.get2BitDecimal(this.price*this.number/100f);
    }
}
