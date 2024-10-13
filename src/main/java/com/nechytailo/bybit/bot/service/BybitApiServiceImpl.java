package com.nechytailo.bybit.bot.service;

import com.google.gson.Gson;
import com.nechytailo.bybit.bot.dto.GetAccountBalanceResponseDto;
import com.nechytailo.bybit.bot.dto.GetCoinBalanceResponseDto;
import com.nechytailo.bybit.bot.dto.ServerTimeDto;
import com.nechytailo.bybit.bot.factory.ProxyRequestService;
import com.nechytailo.bybit.bot.entity.Account;
import com.nechytailo.bybit.bot.model.TradeSide;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Autowired
    private DelayService delayService;

    @Override
    public void placeMarketOrderWithQty(Account account, String symbol, String side, String quantity) {
        long startMethodTime = System.currentTimeMillis();

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

        long startResponseTime = System.currentTimeMillis();
        ResponseEntity<String> response = restTemplate.postForEntity(url, new HttpEntity<>(paramsJson, headers), String.class);
        long endTime = System.currentTimeMillis();
        LOG.debug("Processing placeMarketOrder method time: {}", (endTime - startMethodTime));
        LOG.debug("Processing placeMarketOrder request time: {}", (endTime - startResponseTime));
        LOG.debug("Response: {}", response);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error placing order: " + response.getStatusCode());
        }
    }

    @Override
    public void instantTrade(Account account, String coinToBuy, String coinForBuy) { // coinForBuy = USDT
        long startMethodTime = System.currentTimeMillis();
        GetCoinBalanceResponseDto getBalanceCoinForBuyResponseDto = getCoinBalance(account, coinForBuy);
        String coinForBuyBalance = roundToDecimal(getBalanceCoinForBuyResponseDto.getResult().getBalance().getWalletBalance());
        placeMarketOrderWithQty(account, coinToBuy+coinForBuy, TradeSide.BUY.toString(), coinForBuyBalance);

        delayService.doHoldDelay();

        GetCoinBalanceResponseDto getBalanceCoinToBuyResponseDto = getCoinBalance(account, coinToBuy);
        String coinToBuyBalance = roundToDecimal(getBalanceCoinToBuyResponseDto.getResult().getBalance().getWalletBalance());
        placeMarketOrderWithQty(account, coinToBuy+coinForBuy, TradeSide.SELL.toString(), coinToBuyBalance);
        long endTime = System.currentTimeMillis();
        LOG.debug("Processing instantTrade method time: {}", (endTime - startMethodTime));
    }

    @Override
    public GetCoinBalanceResponseDto getCoinBalance(Account account, String token) {
        long startMethodTime = System.currentTimeMillis();
        RestTemplate restTemplate = proxyRequestService.createRestTemplateWithProxy(account.getProxyParams()); //TODO account == null
        String timestamp = String.valueOf(getServerTime(restTemplate));
        Map<String, Object> params = new HashMap<>();
        params.put("accountType", accountType);
        params.put("coin", token);
        String signature = signService.signGet(account.getApiKey(), account.getApiSecret(), timestamp, params);

        String url = urls.getCoinBalanceUrl(accountType, token);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-BAPI-API-KEY", account.getApiKey());
        headers.set("X-BAPI-SIGN", signature);
        headers.set("X-BAPI-TIMESTAMP", timestamp);
        headers.set("X-BAPI-RECV-WINDOW", recvWindow);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        long startResponseTime = System.currentTimeMillis();
        ResponseEntity<GetCoinBalanceResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, GetCoinBalanceResponseDto.class);
        long endTime = System.currentTimeMillis();
        LOG.debug("Processing getCoinBalance method time: {}", (endTime - startMethodTime));
        LOG.debug("Processing getCoinBalance request time: {}", (endTime - startResponseTime));
        LOG.debug("Response: {}", response);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error getting balance: " + response.getStatusCode()); //TODO add new exception
        }
        return response.getBody();
    }

    @Override
    public GetAccountBalanceResponseDto getAccountBalances(Account account) {
        long startMethodTime = System.currentTimeMillis();
        RestTemplate restTemplate = proxyRequestService.createRestTemplateWithProxy(account.getProxyParams()); //TODO account == null
        String timestamp = String.valueOf(getServerTime(restTemplate));
        Map<String, Object> params = new HashMap<>();
        params.put("accountType", accountType);
        String signature = signService.signGet(account.getApiKey(), account.getApiSecret(), timestamp, params);

        String url = urls.getAccountBalancesUrl(accountType);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-BAPI-API-KEY", account.getApiKey());
        headers.set("X-BAPI-SIGN", signature);
        headers.set("X-BAPI-TIMESTAMP", timestamp);
        headers.set("X-BAPI-RECV-WINDOW", recvWindow);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        long startResponseTime = System.currentTimeMillis();
        ResponseEntity<GetAccountBalanceResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, GetAccountBalanceResponseDto.class);
        long endTime = System.currentTimeMillis();
        LOG.debug("Processing getAccountBalances method time: {}", (endTime - startMethodTime));
        LOG.debug("Processing getAccountBalances request time: {}", (endTime - startResponseTime));
        LOG.debug("Response: {}", response);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error getting balance: " + response.getStatusCode()); //TODO add new exception
        }
        return response.getBody();
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

    private String roundToDecimal(String qty) {
        BigDecimal number = new BigDecimal(qty);
        BigDecimal roundedNumber = number.setScale(5, RoundingMode.DOWN);
        return roundedNumber.toPlainString();
    }

}
