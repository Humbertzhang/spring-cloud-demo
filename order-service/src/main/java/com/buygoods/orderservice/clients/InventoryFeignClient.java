package com.buygoods.orderservice.clients;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient("inventory-service")
public interface InventoryFeignClient {
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/v1/inventory/",
            consumes = "application/json")
    Map<String, Integer> getInventory();
}
