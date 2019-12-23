package com.cloudcreativity.cashiersystem.entity;

/**
 * 数据同步实体类
 */
public class DownloadGoodsData {

    private long goodsId;
    private String goodsName;
    private String unit;
    private String standards;
    private int price;
    private double computerStock;
    private String tab;
    private String categoryOneId;
    private String categoryTwoId;
    private int discountRate;
    private String barCode;

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStandards() {
        return standards;
    }

    public void setStandards(String standards) {
        this.standards = standards;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getCategoryOneId() {
        return categoryOneId;
    }

    public void setCategoryOneId(String categoryOneId) {
        this.categoryOneId = categoryOneId;
    }

    public String getCategoryTwoId() {
        return categoryTwoId;
    }

    public void setCategoryTwoId(String categoryTwoId) {
        this.categoryTwoId = categoryTwoId;
    }

    public double getComputerStock() {
        return computerStock;
    }

    public void setComputerStock(double computerStock) {
        this.computerStock = computerStock;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
