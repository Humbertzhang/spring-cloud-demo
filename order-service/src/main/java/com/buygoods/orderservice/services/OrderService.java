package com.buygoods.orderservice.services;


import com.buygoods.orderservice.clients.InventoryFeignClient;
import com.buygoods.orderservice.clients.PriceDiscoveryClient;
import com.buygoods.orderservice.models.Amount;
import com.buygoods.orderservice.models.Order;
import com.buygoods.orderservice.models.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderService {
    @Value("${amount.adjustment}")
    // 总价格*adjustment等于最终价格.
    private Double adjustment = 1.0;

    @Autowired
    PriceDiscoveryClient priceDiscoveryClient;

    @Autowired
    InventoryFeignClient inventoryFeignClient;

    public Amount getOrderAmount(Orders orders) {
        Map<String, Integer> prices = priceDiscoveryClient.getPricePairs();
        Map<String, Integer> inventories = inventoryFeignClient.getInventory();

        System.out.println(prices.toString());
        System.out.println(inventories.toString());
        // 0.TODO heck if the key of two map are same

        Amount amount = new Amount(0.0);
        // < 1 means bad amount
        Amount badAmount = new Amount(-1.0);

        for(Order order:orders.getOrders()) {
            // Java get 一个不存在的值返回null
            Integer price = prices.get(order.getName());
            Integer inventory = inventories.get(order.getName());
            // 没有相应的货物
            if (price == null || inventory == null) {
                System.out.println("No Such item:" + order.getName());
                return badAmount;
            }
            // 库存不足
            if(inventory < order.getCount()) {
                System.out.println("Item : " + order.getName() + " inventory is " + order.getCount() );
                return badAmount;
            }

            Double subtotal = (double) (price * order.getCount());
            amount.addAmount(subtotal);
        }
        amount.performAdjustment(adjustment);
        return amount;
    }
}
