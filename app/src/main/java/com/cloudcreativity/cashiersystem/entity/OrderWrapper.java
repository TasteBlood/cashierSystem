package com.cloudcreativity.cashiersystem.entity;

import java.util.List;

public class OrderWrapper {
    private List<OrderEntity> records;

    public List<OrderEntity> getRecords() {
        return records;
    }

    public void setRecords(List<OrderEntity> records) {
        this.records = records;
    }
}
