package com.cloudcreativity.cashiersystem.entity;

import com.cloudcreativity.cashiersystem.utils.StrUtils;

import java.util.List;

public class PayDetailEntity {

    private int id;
    private String orderId;
    private long shopId;
    private int finalMoney;
    private int payMoney;
    private int zeroMoney;
    private int integralMoney;
    private String memberId;
    private int payId;
    private String shopName;
    private String adminName;
    private List<OpenOrderGoodsEntity> orderDetails;
    private OrderEntity orderDomain;
    private String createTime;
    private String discount;
    private long adminId;
    private String orderNo;
    private String goodsId;
    private String goodsName;
    private String mobile;
    private String remark;

    public String formatTotal(){
        return "￥"+ StrUtils.get2BitDecimal(this.orderDomain.getTotalMoney()/100f);
    }

    public String formatDiscount(){
        return "折扣终价(-"+StrUtils.get2BitDecimal(this.orderDomain.getDiscountMoney()/100f)+"):";
    }

    public String formatFinal(){
        return "￥"+StrUtils.get2BitDecimal(this.finalMoney/100f);
    }

    public String formatPay(){
        return "￥"+StrUtils.get2BitDecimal(this.payMoney/100f);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public List<OpenOrderGoodsEntity> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OpenOrderGoodsEntity> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public OrderEntity getOrderDomain() {
        return orderDomain;
    }

    public void setOrderDomain(OrderEntity orderDomain) {
        this.orderDomain = orderDomain;
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
