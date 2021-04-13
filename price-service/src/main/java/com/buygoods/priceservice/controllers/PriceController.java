package com.buygoods.priceservice.controllers;

import com.buygoods.priceservice.services.PriceService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "v1/prices")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Map<String, Integer> getPricePairs() {
        Map<String, Integer> pricePairs = priceService.getPricePairs();
        return pricePairs;
    }
}
