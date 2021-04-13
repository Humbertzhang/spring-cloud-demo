package com.buygoods.priceservice.services;

import com.buygoods.priceservice.config.ServiceConfig;
import com.buygoods.priceservice.models.Price;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PriceService {

    @Autowired
    ServiceConfig config;

    @HystrixCommand(commandProperties = {
            // 对超时时间进行配置
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
                             value = "1000"),
            // 在统计窗口内要有的调用数量
            @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"),
            // 调用失败的百分比
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="75"),
            // 开始调用服务以判断服务是否健康之前Hystrix的休眠时间 (ms)
            @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="7000"),
            // 监控服务调用问题的窗口大小。默认为10000ms
            @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="15000"),
            //
            @HystrixProperty(name="metrics.rollingStats.numBuckets", value="5")},
            // 服务失败后的后备方法.
            fallbackMethod = "buildFallbackPricePairs")
    public Map<String, Integer> getPricePairs() {
        // randomSleep for test HystrixCommand
        // randomSleep();
        Map<String, Integer> pairs = new HashMap<String, Integer>();
        for (Price p : config.getPrices()) {
            pairs.put(p.getName(), p.getPrice());
        }
        return pairs;
    }

    private Map<String, Integer> buildFallbackPricePairs() {
        Map<String, Integer> pairs = new HashMap<String, Integer>();
        pairs.put("break", 1000);
        return pairs;
    }

    private void randomSleep() {
        Random rand = new Random();
        int randInt = rand.nextInt(3) + 1;
        if (randInt == 3) {
            this.sleep();
        }
    }

    private void sleep() {
        try {
            // Hystrix limit it to 1000ms
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
