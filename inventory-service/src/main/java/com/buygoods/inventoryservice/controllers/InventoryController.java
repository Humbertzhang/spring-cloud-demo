package com.buygoods.inventoryservice.controllers;

import com.buygoods.inventoryservice.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "v1/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Map<String, Integer> getInventoryPairs() {
        Map<String, Integer> inventoryPairs = inventoryService.getInventoryPairs();
        return inventoryPairs;
    }
}