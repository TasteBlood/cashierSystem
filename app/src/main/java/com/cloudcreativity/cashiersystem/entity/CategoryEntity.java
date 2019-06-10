package com.cloudcreativity.cashiersystem.entity;

import java.util.List;

public class CategoryEntity {
    private String name;
    private int id;
    private List<CategoryEntity> child;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CategoryEntity> getChild() {
        return child;
    }

    public void setChild(List<CategoryEntity> child) {
        this.child = child;
    }
}
