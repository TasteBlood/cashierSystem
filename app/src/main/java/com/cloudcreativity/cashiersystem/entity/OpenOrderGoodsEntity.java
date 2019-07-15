package com.cloudcreativity.cashiersystem.entity;

import com.cloudcreativity.cashiersystem.utils.StrUtils;

/**
 * 开单商品
 */
public class OpenOrderGoodsEntity {

    private long goodsId;
    private String goodsName;
    private String unit;
    private String categoryOneId;
    private int price;
    private float amount;
    private int money;
    private float discount;
    private String standards;

    public String getStandards() {
        return standards;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setStandards(String standards) {
        this.standards = standards;
    }

    public String getCategoryOneId() {
        return categoryOneId;
    }

    public void setCategoryOneId(String categoryOneId) {
        this.categoryOneId = categoryOneId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
        if(this.discount==0){
            return "￥"+StrUtils.get2BitDecimal(this.price*this.amount/100f);
        }else{
            return "￥"+StrUtils.get2BitDecimal(this.price*this.amount*this.discount/100f);
        }
    }

    public void calculateMoney(){
        if(this.discount==0){
            this.money = Float.valueOf(this.price*this.amount).intValue();
        }else{
            this.money = Float.valueOf(this.price*this.amount*this.discount).intValue();
        }
    }
}
