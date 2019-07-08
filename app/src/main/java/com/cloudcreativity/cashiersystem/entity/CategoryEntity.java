package com.cloudcreativity.cashiersystem.entity;

import java.util.List;

public class CategoryEntity {
    private String name;
    private String id;
    private String categoryOneId;
    private List<CategoryEntity> seconds;

    public String getCategoryOneId() {
        return categoryOneId;
    }

    public void setCategoryOneId(String categoryOneId) {
        this.categoryOneId = categoryOneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CategoryEntity> getSeconds() {
        return seconds;
    }

    public void setSeconds(List<CategoryEntity> seconds) {
        this.seconds = seconds;
    }
}
