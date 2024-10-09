package com.nechytailo.bybit.bot.service;

import com.google.gson.Gson;
import com.nechytailo.bybit.bot.dto.ServerTimeDto;
import com.nechytailo.bybit.bot.factory.ProxyRequestService;
import com.nechytailo.bybit.bot.entity.Account;
import com.nechytailo.bybit.bot.utils.URLs;
import com.nechytailo.bybit.bot.utils.UniqueOrderLinkIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class BybitApiServiceImpl implements BybitApiService {

    private static final Logger LOG = LoggerFactory.getLogger(BybitApiServiceImpl.class);

    @Value("${bybit.api.accountType}")
    private String accountType;

    @Value("${bybit.recv_window:20000}")
    private String recvWindow;

    @Autowired
    private URLs urls;

    @Autowired
    private SignService signService;

    @Autowired
    private ProxyRequestService proxyRequestService;

    @Override
    public void placeMarketOrder(Account account, String symbol, String side, String quantity) {
        RestTemplate restTemplate = proxyRequestService.createRestTemplateWithProxy(account.getProxyParams()); //TODO account == null
        String timestamp = String.valueOf(getServerTime(restTemplate));

        Map<String, Object> params = new HashMap<>();
        params.put("category", "spot");
        params.put("symbol", symbol);
        params.put("side", side.toUpperCase());
        params.put("orderType", "Market");
        params.put("qty", quantity);
        String orderLinkId = UniqueOrderLinkIdGenerator.createUniqueOrderLinkId();
        params.put("orderLinkId", orderLinkId);

        String paramsJson = new Gson().toJson(params);
        String signature = signService.signPost(account.getApiKey(), account.getApiSecret(), timestamp+"", params); //TODO
        String url = urls.getCreateOrderUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-BAPI-API-KEY", account.getApiKey());
        headers.set("X-BAPI-SIGN", signature);
        headers.set("X-BAPI-TIMESTAMP", String.valueOf(timestamp));
        headers.set("X-BAPI-RECV-WINDOW", recvWindow);
        headers.set("Content-Type", "application/json");

        long startTime = System.currentTimeMillis();
        ResponseEntity<String> response = restTemplate.postForEntity(url, new HttpEntity<>(paramsJson, headers), String.class);
        long endTime = System.currentTimeMillis();
        LOG.debug("Processing time: {}", (endTime - startTime));
        LOG.debug("Response: {}", response);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error placing order: " + response.getStatusCode());
        }
    }

    @Override
    public String getCoinBalance(Account account, String token) {
        RestTemplate restTemplate = proxyRequestService.createRestTemplateWithProxy(account.getProxyParams()); //TODO account == null
        String timestamp = String.valueOf(getServerTime(restTemplate));
        Map<String, Object> params = new HashMap<>();
        params.put("accountType", accountType);
        params.put("coin", token);
        String signature = signService.signGet(account.getApiKey(), account.getApiSecret(), timestamp, params);

        String url = urls.getCoinBalanceUrl(accountType, token);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-BAPI-API-KEY", account.getApiKey());
            headers.set("X-BAPI-SIGN", signature);
            headers.set("X-BAPI-TIMESTAMP", timestamp);
            headers.set("X-BAPI-RECV-WINDOW", recvWindow);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            LOG.debug("Response: {}", response);
            return response.getBody();
        } catch (Exception e) {
            return "Error retrieving balance for account "; //TODO
        }
    }

    public Long getServerTime(RestTemplate restTemplate) {
        String url = urls.getSystemTimeUrl();
        try {
            ResponseEntity<ServerTimeDto> response = restTemplate.getForEntity(url, ServerTimeDto.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                ServerTimeDto serverTimeDto = response.getBody();
                return Long.parseLong(serverTimeDto.getTimeResultDto().getTimeSecond()) * 1000; //TODO not null and other validation
            }
        } catch (Exception e) {
            e.printStackTrace(); //TODO
        }
        return null; //TODO
    }

}
