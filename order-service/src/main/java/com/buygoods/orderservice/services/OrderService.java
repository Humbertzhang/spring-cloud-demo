package com.buygoods.orderservice.services;


import com.buygoods.orderservice.clients.InventoryFeignClient;
import com.buygoods.orderservice.clients.PriceDiscoveryClient;
import com.buygoods.orderservice.models.Amount;
import com.buygoods.orderservice.models.Order;
import com.buygoods.orderservice.models.Orders;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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

    @HystrixCommand(fallbackMethod = "buildFallbackAmount",
                    commandProperties = {
                    // 对超时时间进行配置
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1000")},
                    // 舱壁模式，即针对这个API中的请求专门创建一个线程池，避免这个API中请求的东西太慢，导致整体的连接池中的线程都被占用。
                    threadPoolKey = "getOrderAmount",
                    threadPoolProperties = {
                        // 线程池中最大数量
                        @HystrixProperty(name = "coreSize", value = "4"),
                        // 在线程池前创建一个队列，在繁忙时最大堵塞的请求数量
                        // 当超过时会直接使用fallbackMethod，而不再等待
                        @HystrixProperty(name = "maxQueueSize", value = "2")
                    })
    public Amount getOrderAmount(Orders orders) {
        Map<String, Integer> prices = this.getPrices();
        Map<String, Integer> inventories = this.getInventory();

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

    public Map<String, Integer> getPrices() {
        return priceDiscoveryClient.getPricePairs();
    }

    public Map<String, Integer> getInventory() {
        return inventoryFeignClient.getInventory();
    }

    private Amount buildFallbackAmount(Orders orders) {
        Amount badAmount = new Amount(-2.0);
        return badAmount;
    }
}
