package com.cloudcreativity.cashiersystem.utils;

import java.util.List;

/**
 *
 *          这是全部请求返回数据的基类
 */
public class BaseResult<T>{
    private List<T> records;
    private int totalPage;
    private int maxResult;
    private int currentPage;

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
