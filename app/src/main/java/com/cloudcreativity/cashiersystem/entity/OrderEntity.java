package com.cloudcreativity.cashiersystem.entity;

public class OrderEntity {
    private String id;
    private double money;
    private int state;
    private String payway;
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String formatWay() {
        switch (this.payway) {
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
