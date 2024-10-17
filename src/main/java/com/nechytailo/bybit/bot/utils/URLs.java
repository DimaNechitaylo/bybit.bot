package com.nechytailo.bybit.bot.utils;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class URLs {

    @Autowired
    private ByBitEndpoints byBitEndpoints;

    @Value("${bybit.url:https://api.bybit.com}")
    private String BYBIT_URL;
    private String systemTimeUrl;
    private String coinBalanceUrl;
    private String createOrderUrl;
    private String accountBalancesUrl;
    private String marketPriceUrl;

    @PostConstruct
    public void init() {
        this.systemTimeUrl = BYBIT_URL + byBitEndpoints.getSystemTimeEndpoint();
        this.coinBalanceUrl = BYBIT_URL + byBitEndpoints.getCoinBalanceEndpoint();
        this.createOrderUrl = BYBIT_URL + byBitEndpoints.getCreateOrderEndpoint();
        this.accountBalancesUrl = BYBIT_URL + byBitEndpoints.getAccountBalancesEndpoint();
        this.marketPriceUrl = BYBIT_URL + byBitEndpoints.getMarketPriceEndpoint();
    }

    public String getCoinBalanceUrl(String accountType, String token) {
        return coinBalanceUrl + "?" + "accountType=" + accountType + "&coin=" + token;
    }

    public String getAccountBalancesUrl(String accountType) {
        return accountBalancesUrl + "?" + "accountType=" + accountType;
    }

    public String getMarketPriceUrl(String symbol, String category) {
        return marketPriceUrl + "?" + "symbol=" + symbol + "&category=" + category;
    }

}
