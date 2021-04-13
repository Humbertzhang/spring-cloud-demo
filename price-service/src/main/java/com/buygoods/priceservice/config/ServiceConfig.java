package com.buygoods.priceservice.config;

import com.buygoods.priceservice.models.Price;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// 将config从service中抽了出来，因为@ConfigurationProperties与Hystrix会产生冲突导致service初始化失败
@Component
@ConfigurationProperties(prefix = "priceconfig")
public class ServiceConfig {
    private List<Price> prices = new ArrayList<Price>();

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

}
