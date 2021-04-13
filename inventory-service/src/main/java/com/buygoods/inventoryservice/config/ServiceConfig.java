package com.buygoods.inventoryservice.config;


import com.buygoods.inventoryservice.models.Inventory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// 将config从service中抽了出来，因为@ConfigurationProperties与Hystrix会产生冲突导致service初始化失败
@Component
@ConfigurationProperties(prefix = "inventoryconfig")
public class ServiceConfig {
    private List<Inventory> inventories = new ArrayList<Inventory>();

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }
}
