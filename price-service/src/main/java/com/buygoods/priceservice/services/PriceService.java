package com.buygoods.priceservice.services;

import com.buygoods.priceservice.models.Price;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@ConfigurationProperties(prefix = "priceconfig")
public class PriceService {
    private List<Price> prices = new ArrayList<Price>();

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public Map<String, Integer> getPricePairs() {
        Map<String, Integer> pairs = new HashMap<String, Integer>();
        for (Price p : this.prices) {
            pairs.put(p.getName(), p.getPrice());
        }
        return pairs;
    }
}
