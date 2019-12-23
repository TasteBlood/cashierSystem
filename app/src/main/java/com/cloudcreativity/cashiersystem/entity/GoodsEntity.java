package com.cloudcreativity.cashiersystem.entity;

import com.cloudcreativity.cashiersystem.utils.StrUtils;

public class GoodsEntity {
    private int id;
    private String categoryOneId;
    private String categoryTwoId;
    private double computerStock;
    private long goodsId;
    private long shopId;
    private double reallyStock;
    private double differenceStock;
    private double peopleLoss;
    private double naturalLoss;
    private String remark;
    private String createTime;
    private String updateTime;
    private GoodsDomain goodsDomain;
    private String goodsName;
    private int rate;

    public GoodsEntity(){

    }

    public GoodsEntity(String categoryOneId, String categoryTwoId, double computerStock, long goodsId, GoodsDomain goodsDomain, String goodsName, int rate) {
        this.categoryOneId = categoryOneId;
        this.categoryTwoId = categoryTwoId;
        this.computerStock = computerStock;
        this.goodsId = goodsId;
        this.goodsDomain = goodsDomain;
        this.goodsName = goodsName;
        this.rate = rate;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCategoryOneId(String categoryOneId) {
        this.categoryOneId = categoryOneId;
    }

    public String getCategoryOneId() {
        return categoryOneId;
    }

    public void setCategoryTwoId(String categoryTwoId) {
        this.categoryTwoId = categoryTwoId;
    }

    public String getCategoryTwoId() {
        return categoryTwoId;
    }

    public void setComputerStock(int computerStock) {
        this.computerStock = computerStock;
    }

    public double getComputerStock() {
        return computerStock;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public long getShopId() {
        return shopId;
    }

    public void setReallyStock(int reallyStock) {
        this.reallyStock = reallyStock;
    }

    public double getReallyStock() {
        return reallyStock;
    }

    public void setDifferenceStock(int differenceStock) {
        this.differenceStock = differenceStock;
    }

    public double getDifferenceStock() {
        return differenceStock;
    }

    public void setPeopleLoss(int peopleLoss) {
        this.peopleLoss = peopleLoss;
    }

    public double getPeopleLoss() {
        return peopleLoss;
    }

    public void setNaturalLoss(int naturalLoss) {
        this.naturalLoss = naturalLoss;
    }

    public double getNaturalLoss() {
        return naturalLoss;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public GoodsDomain getGoodsDomain() {
        return goodsDomain;
    }

    public void setGoodsDomain(GoodsDomain goodsDomain) {
        this.goodsDomain = goodsDomain;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    /**
     * Auto-generated: 2019-07-02 15:57:4
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public static class GoodsDomain {

        private long id;
        private String name;
        private String categoryOneId;
        private String categoryTwoId;
        private String smallImg;
        private String bigImg;
        private String tab;
        private String standards;
        private String unit;
        private String remark;
        private String qualityDate;
        private int freshDate;
        private long areaId;
        private long cityId;
        private long provinceId;
        private String countryId;
        private int state;
        private int purchasePrice;
        private int sellPrice;
        private int price;
        private String createTime;
        private String updateTime;
        private String isShelf;
        private String deleteState;
        private String barCode;

        public GoodsDomain(){

        }

        public GoodsDomain(long id, String name, String categoryOneId, String categoryTwoId, String tab, String standards, String unit, int price, String barCode) {
            this.id = id;
            this.name = name;
            this.categoryOneId = categoryOneId;
            this.categoryTwoId = categoryTwoId;
            this.tab = tab;
            this.standards = standards;
            this.unit = unit;
            this.price = price;
            this.barCode = barCode;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
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

        public void setSmallImg(String smallImg) {
            this.smallImg = smallImg;
        }

        public String getSmallImg() {
            return smallImg;
        }

        public void setBigImg(String bigImg) {
            this.bigImg = bigImg;
        }

        public String getBigImg() {
            return bigImg;
        }

        public void setTab(String tab) {
            this.tab = tab;
        }

        public String getTab() {
            return tab;
        }

        public void setStandards(String standards) {
            this.standards = standards;
        }

        public String getStandards() {
            return standards;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getUnit() {
            return unit;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }

        public void setQualityDate(String qualityDate) {
            this.qualityDate = qualityDate;
        }

        public String getQualityDate() {
            return qualityDate;
        }

        public void setFreshDate(int freshDate) {
            this.freshDate = freshDate;
        }

        public int getFreshDate() {
            return freshDate;
        }

        public void setAreaId(long areaId) {
            this.areaId = areaId;
        }

        public long getAreaId() {
            return areaId;
        }

        public void setCityId(long cityId) {
            this.cityId = cityId;
        }

        public long getCityId() {
            return cityId;
        }

        public void setProvinceId(long provinceId) {
            this.provinceId = provinceId;
        }

        public long getProvinceId() {
            return provinceId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }

        public void setPurchasePrice(int purchasePrice) {
            this.purchasePrice = purchasePrice;
        }

        public int getPurchasePrice() {
            return purchasePrice;
        }

        public void setSellPrice(int sellPrice) {
            this.sellPrice = sellPrice;
        }

        public int getSellPrice() {
            return sellPrice;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getPrice() {
            return price;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setIsShelf(String isShelf) {
            this.isShelf = isShelf;
        }

        public String getIsShelf() {
            return isShelf;
        }

        public void setDeleteState(String deleteState) {
            this.deleteState = deleteState;
        }

        public String getDeleteState() {
            return deleteState;
        }

        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }

        public String getBarCode() {
            return barCode;
        }

        public String formatPrice() {
            return "￥" + (StrUtils.get2BitDecimal(this.price / 100f));
        }

    }

}
