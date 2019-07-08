package com.cloudcreativity.cashiersystem.entity;

import java.util.List;

public class OrderDetailEntity {
    private String orderNum;
    private Long memberId;
    private String identity;
    private int state;
    private double totalMoney;
    private double discountMoney;
    private String pawWay;
    private String remark;
    private String createTime;

    private List<OrderItemEntity> items;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(double discountMoney) {
        this.discountMoney = discountMoney;
    }

    public String getPawWay() {
        return pawWay;
    }

    public void setPawWay(String pawWay) {
        this.pawWay = pawWay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<OrderItemEntity> getItems() {
        return items;
    }

    public void setItems(List<OrderItemEntity> items) {
        this.items = items;
    }

    public String formatWay() {
        switch (this.pawWay) {
            case "1":
                return "手机";
            case "2":
                return "现金";
            case "3":
                return "刷卡";
            case "4":
                return "余额";
            default:
                return "未知";
        }
    }
}
