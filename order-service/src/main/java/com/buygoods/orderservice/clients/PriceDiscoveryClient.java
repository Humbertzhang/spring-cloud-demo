package com.buygoods.orderservice.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class PriceDiscoveryClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    /*
    * DiscoveryClient是最底层的库。
    * */
    public Map<String, Integer> getPricePairs() {
        RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("price-service");
        if (instances.size()==0) {
            return null;
        }
        // 需要手动获取、拼接地址
        String serviceUri = String.format("%s/v1/prices/",instances.get(0).getUri().toString());

        // 对于Map需要使用Type Reference预先构建
        ParameterizedTypeReference<Map<String, Integer>> responseType =
                new ParameterizedTypeReference<Map<String, Integer>>() {};

        ResponseEntity< Map<String, Integer> > restExchange = restTemplate.exchange(
                serviceUri,
                HttpMethod.GET,
                null,
                responseType);

        return restExchange.getBody();
    }
}
