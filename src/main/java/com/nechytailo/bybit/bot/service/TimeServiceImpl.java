package com.nechytailo.bybit.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class TimeServiceImpl implements TimeService{

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long getServerTime() {
        String url = "https://api.bybit.com/v5/market/time";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return (long) response.getBody().get("result.timeSecond") * 1000;
        }
        throw new RuntimeException("Error fetching server time: " + response.getStatusCode());

    }
}
