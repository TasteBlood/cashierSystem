package com.cloudcreativity.cashiersystem.entity;

import com.cloudcreativity.cashiersystem.utils.StrUtils;

public class GoodsEntity {
    private int id;
    private String categoryOneId;
    private String categoryTwoId;
    private int computerStock;
    private long goodsId;
    private long shopId;
    private int reallyStock;
    private int differenceStock;
    private int peopleLoss;
    private int naturalLoss;
    private String remark;
    private String createTime;
    private String updateTime;
    private GoodsDomain goodsDomain;
    private String goodsName;

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

    public int getComputerStock() {
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

    public int getReallyStock() {
        return reallyStock;
    }

    public void setDifferenceStock(int differenceStock) {
        this.differenceStock = differenceStock;
    }

    public int getDifferenceStock() {
        return differenceStock;
    }

    public void setPeopleLoss(int peopleLoss) {
        this.peopleLoss = peopleLoss;
    }

    public int getPeopleLoss() {
        return peopleLoss;
    }

    public void setNaturalLoss(int naturalLoss) {
        this.naturalLoss = naturalLoss;
    }

    public int getNaturalLoss() {
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
    public class GoodsDomain {

        private long id;
        private String name;
        private int categoryOneId;
        private int categoryTwoId;
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

        public void setCategoryOneId(int categoryOneId) {
            this.categoryOneId = categoryOneId;
        }

        public int getCategoryOneId() {
            return categoryOneId;
        }

        public void setCategoryTwoId(int categoryTwoId) {
            this.categoryTwoId = categoryTwoId;
        }

        public int getCategoryTwoId() {
            return categoryTwoId;
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
