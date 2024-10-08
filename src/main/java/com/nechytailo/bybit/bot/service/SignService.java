package com.nechytailo.bybit.bot.service;

import java.util.Map;

public interface SignService {
    public String signGet(String apiKey, String apiSecret, String timestamp, Map<String, Object> params);
    public String signPost(String apiKey, String apiSecret, String timestamp, Map<String, Object> params);
}
