package com.nechytailo.bybit.bot.service;

import com.google.gson.Gson;
import com.nechytailo.bybit.bot.dto.GetAccountBalanceResponseDto;
import com.nechytailo.bybit.bot.dto.GetCoinBalanceResponseDto;
import com.nechytailo.bybit.bot.dto.GetMarketPriceResponseDto;
import com.nechytailo.bybit.bot.dto.ServerTimeDto;
import com.nechytailo.bybit.bot.factory.ProxyRequestService;
import com.nechytailo.bybit.bot.entity.Account;
import com.nechytailo.bybit.bot.model.TradeSide;
import com.nechytailo.bybit.bot.utils.*;
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

    private String accountType;

    private String recvWindow;

    private URLs urls;

    private SignService signService;

    private ProxyRequestService proxyRequestService;

    private DelayService delayService;
    private PriceCalculator priceCalculator;

    @Autowired
    public BybitApiServiceImpl(@Value("${bybit.api.accountType}") String accountType,
                               @Value("${bybit.recv_window:20000}") String recvWindow,
                               URLs urls,
                               SignService signService,
                               ProxyRequestService proxyRequestService,
                               DelayService delayService,
                               PriceCalculator priceCalculator) {
        this.accountType = accountType;
        this.recvWindow = recvWindow;
        this.urls = urls;
        this.signService = signService;
        this.proxyRequestService = proxyRequestService;
        this.delayService = delayService;
        this.priceCalculator = priceCalculator;
    }

    @Override
    public void placeMarketOrderWithQty(Account account, String symbol, String side, String quantity) {
        long startMethodTime = System.currentTimeMillis();
        LOG.debug("placeMarketOrderWithQty parameters: Account: {}, {} {} {}", account.getApiKey(), side, symbol, quantity);

        RestTemplate restTemplate = proxyRequestService.createRestTemplateWithProxy(account.getProxyParams()); //TODO account == null
        String timestamp = String.valueOf(getServerTime(restTemplate));

        Map<String, Object> params = new HashMap<>();
        params.put("category", "spot");
        params.put("symbol", symbol);
        params.put("side", side.toUpperCase());
        params.put("orderType", Constants.ORDER_TYPE_MARKET);
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
        LOG.debug("Processing placeMarketOrder request time: {}, method time: {}", (endTime - startResponseTime), (endTime - startMethodTime));
        LOG.debug("placeMarketOrderWithQty Response: {}", response);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error placing order: " + response.getStatusCode());
        }
    }

    @Override
    public void placeLimitOrderWithQty(Account account, String symbol, String side, String quantity, String price) {
        long startMethodTime = System.currentTimeMillis();
        LOG.debug("placeLimitOrderWithQty parameters: Account: {}, {} {} {}", account.getApiKey(), side, symbol, quantity);

        RestTemplate restTemplate = proxyRequestService.createRestTemplateWithProxy(account.getProxyParams()); //TODO account == null
        String timestamp = String.valueOf(getServerTime(restTemplate));

        Map<String, Object> params = new HashMap<>();
        params.put("category", "spot");
        params.put("symbol", symbol);
        params.put("side", side.toUpperCase());
        params.put("orderType", Constants.ORDER_TYPE_LIMIT);
        params.put("qty", quantity);
        params.put("price", price);
        params.put("timeInForce", "IOC");
        params.put("marketUnit", "quoteCoin");
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

        long endProcessDataMethodTime = System.currentTimeMillis();
        LOG.debug("@@@@@@@@ Processing Data for placeLimitOrderWithQty  time: {}", (endProcessDataMethodTime - startMethodTime));

        long startResponseTime = System.currentTimeMillis();
        ResponseEntity<String> response = restTemplate.postForEntity(url, new HttpEntity<>(paramsJson, headers), String.class);
        long endTime = System.currentTimeMillis();
        LOG.debug("Processing placeLimitOrderWithQty request time: {}, method time: {}", (endTime - startResponseTime), (endTime - startMethodTime));
        LOG.debug("placeLimitOrderWithQty Response: {}", response);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error placing order: " + response.getStatusCode());
        }
    }

    @Override
    public void instantTradeMarket(Account account, String coinToBuy, String coinForBuy) { // coinForBuy = USDT
        LOG.debug("______________________________________________");
        LOG.debug("Start instantTradeMarket for account: {}", account.getId());
        long startMethodTime = System.currentTimeMillis();
        GetCoinBalanceResponseDto getBalanceCoinForBuyResponseDto = getCoinBalance(account, coinForBuy);
        String coinForBuyBalance = Rounder.roundToDecimal(getBalanceCoinForBuyResponseDto.getResult().getBalance().getWalletBalance());
        placeMarketOrderWithQty(account, coinToBuy+coinForBuy, TradeSide.BUY.toString(), coinForBuyBalance);

        delayService.doHoldDelay();

        GetCoinBalanceResponseDto getBalanceCoinToBuyResponseDto = getCoinBalance(account, coinToBuy);
        String coinToBuyBalance = Rounder.roundToDecimal(getBalanceCoinToBuyResponseDto.getResult().getBalance().getWalletBalance());
        placeMarketOrderWithQty(account, coinToBuy+coinForBuy, TradeSide.SELL.toString(), coinToBuyBalance);
        long endTime = System.currentTimeMillis();
        LOG.debug("Processing instantTradeMarket method time: {}, for account: {}", (endTime - startMethodTime), account.getId());
        LOG.debug("______________________________________________");
    }

    @Override
    public void instantTradeLimit(Account account, String coinToBuy, String coinForBuy) { // coinForBuy = USDT
        LOG.debug("______________________________________________");
        LOG.debug("Start instantTradeLimit for account: {}", account.getId());
        long startMethodTime = System.currentTimeMillis();
        String symbol = coinToBuy+coinForBuy;

        GetCoinBalanceResponseDto getBalanceCoinForBuyResponseDto = getCoinBalance(account, coinForBuy);
        String coinForBuyBalance = getBalanceCoinForBuyResponseDto.getResult().getBalance().getWalletBalance();
        LOG.debug("Balance of {}: {}", coinForBuy, coinForBuyBalance);

        long startGetMarketPrice = System.currentTimeMillis();
        GetMarketPriceResponseDto getMarketPriceResponseDto = getMarketPrice(account, symbol);
        String marketPriceForBuy = getMarketPriceResponseDto.getResult().getMarketDataList().get(0).getLastPrice();
        String priceToBuy = priceCalculator.getIncreasedPrice(marketPriceForBuy);
        String priceToBuyFormated = Rounder.roundToDecimal(priceToBuy); //TODO
        LOG.debug("Market price of {} for buy: {}, increased price: {}, formatted: {}", symbol, marketPriceForBuy, priceToBuy, priceToBuyFormated);

        String qtyToBuy = priceCalculator.calculateQtyOfTargetCoinToBuy(priceToBuyFormated, coinForBuyBalance);
        LOG.debug("Qty {} to buy: {}", coinToBuy, qtyToBuy);
        long endGetMarketPrice = System.currentTimeMillis();
        LOG.debug("@@@@@@@@@ process MarketPrice time {}, for account: {}", (endGetMarketPrice - startGetMarketPrice), account.getId());
        LOG.debug("Create limit order to buy {} at a price of {} in quantity of {} pieces", coinToBuy, priceToBuyFormated, qtyToBuy);

        placeLimitOrderWithQty(account, symbol, TradeSide.BUY.toString(), qtyToBuy, priceToBuyFormated);

        delayService.doHoldDelay();

        GetCoinBalanceResponseDto getBalanceCoinToBuyResponseDto = getCoinBalance(account, coinToBuy);
        String coinToBuyBalance = Rounder.roundToDecimal(getBalanceCoinToBuyResponseDto.getResult().getBalance().getWalletBalance());
        LOG.debug("Balance of {}: {}", coinToBuy, coinToBuyBalance);

        GetMarketPriceResponseDto getMarketPriceResponseDtoSell = getMarketPrice(account, symbol);
        String marketPriceForSell = getMarketPriceResponseDtoSell.getResult().getMarketDataList().get(0).getLastPrice();
        String priceForSell = priceCalculator.getDecreasedPrice(marketPriceForSell);
        String priceForSellFormated = Rounder.roundToDecimal(priceForSell);
        LOG.debug("Market price of {} for sell: {}, decreased price: {}, formatted: {}", symbol, marketPriceForSell, priceForSell, priceForSellFormated);

        LOG.debug("Create limit order to sell {} at a price of {} in quantity of {} pieces", coinToBuy, priceForSellFormated, coinToBuyBalance);
        placeLimitOrderWithQty(account, symbol, TradeSide.SELL.toString(), coinToBuyBalance, priceForSellFormated);
        long endTime = System.currentTimeMillis();
        LOG.debug("Processing instantTradeLimit method time: {}, for account: {}", (endTime - startMethodTime), account.getId());
        LOG.debug("______________________________________________");
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
        LOG.debug("Processing getCoinBalance request time: {}, method time: {}", (endTime - startResponseTime), (endTime - startMethodTime));
        LOG.debug("Response: {}", response);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error getting balance: " + response.getStatusCode()); //TODO add new exception
        }
        return response.getBody();
    }

    @Override
    public GetMarketPriceResponseDto getMarketPrice(Account account, String symbol) {
        long startMethodTime = System.currentTimeMillis();
        RestTemplate restTemplate = proxyRequestService.createRestTemplateWithProxy(account.getProxyParams()); //TODO не создавать каждый раз заново для процесса одного акка, account == null
        String timestamp = String.valueOf(startMethodTime); //TODO
        Map<String, Object> params = new HashMap<>();
        params.put("category", "spot");
        params.put("symbol", symbol);
        String signature = signService.signGet(account.getApiKey(), account.getApiSecret(), timestamp, params);
        String url = urls.getMarketPriceUrl(symbol,"spot");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-BAPI-API-KEY", account.getApiKey());
        headers.set("X-BAPI-SIGN", signature);
        headers.set("X-BAPI-TIMESTAMP", timestamp);
        headers.set("X-BAPI-RECV-WINDOW", recvWindow);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        long startResponseTime = System.currentTimeMillis();
        ResponseEntity<GetMarketPriceResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, GetMarketPriceResponseDto.class);
        long endTime = System.currentTimeMillis();
        LOG.debug("Processing getMarketPrice request time: {}, method time: {}", (endTime - startResponseTime), (endTime - startMethodTime));
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
        LOG.debug("Processing getAccountBalances request time: {}, method time: {}", (endTime - startResponseTime), (endTime - startMethodTime));
        LOG.debug("GetAccountBalance Response: {}", response);
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


}
