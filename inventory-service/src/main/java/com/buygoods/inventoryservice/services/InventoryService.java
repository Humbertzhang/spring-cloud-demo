package com.buygoods.inventoryservice.services;

import com.buygoods.inventoryservice.models.Inventory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@ConfigurationProperties(prefix = "inventoryconfig")
public class InventoryService {
    private List<Inventory> inventories = new ArrayList<Inventory>();

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

    public Map<String, Integer> getInventoryPairs() {
        Map<String, Integer> pairs = new HashMap<String, Integer>();
        for (Inventory i : this.inventories) {
            pairs.put(i.getName(), i.getCount());
        }
        return pairs;
    }
}
