package com.cloudcreativity.cashiersystem.entity;

public class CashLogEntity {
    private int id;
    private int orderId;
    private long shopId;
    private int finalMoney;
    private int payMoney;
    private int zeroMoney;
    private int integralMoney;
    private String memberId;
    private int payId;
    private String createTime;
    private String discount;
    private long adminId;
    private String orderNo;
    private String goodsId;
    private String goodsName;
    private String mobile;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public int getFinalMoney() {
        return finalMoney;
    }

    public void setFinalMoney(int finalMoney) {
        this.finalMoney = finalMoney;
    }

    public int getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(int payMoney) {
        this.payMoney = payMoney;
    }

    public int getZeroMoney() {
        return zeroMoney;
    }

    public void setZeroMoney(int zeroMoney) {
        this.zeroMoney = zeroMoney;
    }

    public int getIntegralMoney() {
        return integralMoney;
    }

    public void setIntegralMoney(int integralMoney) {
        this.integralMoney = integralMoney;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
