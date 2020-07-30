package com.buygoods.orderservice.controllers;

import com.buygoods.orderservice.models.Amount;
import com.buygoods.orderservice.models.Orders;
import com.buygoods.orderservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Amount getOrderAmount(@RequestBody Orders orders) {
        return orderService.getOrderAmount(orders);
    }
}
