package com.cloudcreativity.cashiersystem.entity;

import com.cloudcreativity.cashiersystem.utils.StrUtils;

import java.util.List;

public class OrderDetailEntity {
    private int id;
    private String orderNo;
    private String amount;
    private int totalMoney;
    private int discountMoney;
    private String status;
    private String type;
    private String createTime;
    private String categoryOneId;
    private String shopId;
    private String updateTime;
    private int check;
    private String inStorage;
    private String payName;
    private MemberEntity memberDomain;
    private ConsumeEntity consumeDomain;
    private List<OpenOrderGoodsEntity> orderDetails;

    public String formatMoney(){
        return "￥"+ StrUtils.get2BitDecimal(totalMoney/100f);
    }

    public String formatFinal(){
        return "￥"+StrUtils.get2BitDecimal((totalMoney-discountMoney)/100f)
                +"(实收：￥"+StrUtils.get2BitDecimal(consumeDomain.getPayMoney()/100f)+")";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(int discountMoney) {
        this.discountMoney = discountMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCategoryOneId() {
        return categoryOneId;
    }

    public void setCategoryOneId(String categoryOneId) {
        this.categoryOneId = categoryOneId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public String getInStorage() {
        return inStorage;
    }

    public void setInStorage(String inStorage) {
        this.inStorage = inStorage;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public MemberEntity getMemberDomain() {
        return memberDomain;
    }

    public void setMemberDomain(MemberEntity memberDomain) {
        this.memberDomain = memberDomain;
    }

    public ConsumeEntity getConsumeDomain() {
        return consumeDomain;
    }

    public void setConsumeDomain(ConsumeEntity consumeDomain) {
        this.consumeDomain = consumeDomain;
    }

    public List<OpenOrderGoodsEntity> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OpenOrderGoodsEntity> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public class ConsumeEntity {
        private String id;
        private String orderId;
        private String shopId;
        private String finalMoney;
        private int payMoney;
        private String zeroMoney;
        private String integralMoney;
        private String memberId;
        private String payId;
        private String createTime;
        private String discount;
        private String adminId;
        private String orderNo;
        private String goodsId;
        private String goodsName;
        private String mobile;
        private String remark;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getFinalMoney() {
            return finalMoney;
        }

        public void setFinalMoney(String finalMoney) {
            this.finalMoney = finalMoney;
        }

        public int getPayMoney() {
            return payMoney;
        }

        public void setPayMoney(int payMoney) {
            this.payMoney = payMoney;
        }

        public String getZeroMoney() {
            return zeroMoney;
        }

        public void setZeroMoney(String zeroMoney) {
            this.zeroMoney = zeroMoney;
        }

        public String getIntegralMoney() {
            return integralMoney;
        }

        public void setIntegralMoney(String integralMoney) {
            this.integralMoney = integralMoney;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getPayId() {
            return payId;
        }

        public void setPayId(String payId) {
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

        public String getAdminId() {
            return adminId;
        }

        public void setAdminId(String adminId) {
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
}
