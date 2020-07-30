package com.buygoods.inventoryservice.models;

public class Inventory {
    private String name;
    private Integer count;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }
}
