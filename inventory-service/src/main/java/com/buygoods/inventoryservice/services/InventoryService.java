package com.buygoods.inventoryservice.services;

import com.buygoods.inventoryservice.config.ServiceConfig;
import com.buygoods.inventoryservice.models.Inventory;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryService {

    @Autowired
    ServiceConfig config;

    @HystrixCommand(commandProperties = {
            // 对超时时间进行配置
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
                    value = "1000")},
            // 服务失败后的后备方法.
            fallbackMethod = "buildFallbackInventoryPairs")
    public Map<String, Integer> getInventoryPairs() {
        Map<String, Integer> pairs = new HashMap<String, Integer>();
        for (Inventory i : this.config.getInventories()) {
            pairs.put(i.getName(), i.getCount());
        }
        return pairs;
    }

    private Map<String, Integer> buildFallbackInventoryPairs() {
        Map<String, Integer> pairs = new HashMap<String, Integer>();
        pairs.put("break", 0);
        return pairs;
    }
}
