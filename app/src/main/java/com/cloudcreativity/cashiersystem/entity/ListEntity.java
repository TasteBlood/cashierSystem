package com.cloudcreativity.cashiersystem.entity;

import com.cloudcreativity.cashiersystem.utils.StrUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 挂单商品信息
 */
public class ListEntity {
    private int id;
    private String mobile;
    private long mid;
    private String memo;
    private double totalMoney;
    private double discountReduce;
    private double discountMoney;
    private List<OpenOrderGoodsEntity> items;

    public ListEntity(String mobile, long mid, String memo, double totalMoney, double discountReduce, double discountMoney, List<OpenOrderGoodsEntity> items) {
        this.mobile = mobile;
        this.mid = mid;
        this.memo = memo;
        this.totalMoney = totalMoney;
        this.discountReduce = discountReduce;
        this.discountMoney = discountMoney;
        this.items = items;
    }

    public String formatTotal(){
        BigDecimal d1 = new BigDecimal(String.valueOf(this.totalMoney));
        BigDecimal d2 = new BigDecimal("100");

        return "￥"+ StrUtils.get2BitDecimal(d1.divide(d2).doubleValue());
    }

    public String formatDisTotal(){
        BigDecimal d1 = new BigDecimal(String.valueOf(this.discountMoney));
        BigDecimal d2 = new BigDecimal("100");

        return "￥"+ StrUtils.get2BitDecimal(d1.divide(d2).doubleValue());
        //return "¥"+ StrUtils.get2BitDecimal(this.discountReduce/100f);
    }

    public String formatDisReduce(){
        BigDecimal d1 = new BigDecimal(String.valueOf(this.discountReduce));
        BigDecimal d2 = new BigDecimal("100");

        return String.valueOf(StrUtils.get2BitDecimal(d1.divide(d2).doubleValue()));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getDiscountReduce() {
        return discountReduce;
    }

    public void setDiscountReduce(double discountReduce) {
        this.discountReduce = discountReduce;
    }

    public double getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(double discountMoney) {
        this.discountMoney = discountMoney;
    }

    public List<OpenOrderGoodsEntity> getItems() {
        return items;
    }

    public void setItems(List<OpenOrderGoodsEntity> items) {
        this.items = items;
    }
}
