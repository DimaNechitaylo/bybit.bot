package com.nechytailo.bybit.bot.controller;

import com.nechytailo.bybit.bot.service.BalanceInfoService;
import com.nechytailo.bybit.bot.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HealthCheckController {

    @Autowired
    BalanceInfoService balanceInfoService;

    @Autowired
    TradingService tradingService;

    @GetMapping("/healthCheck")
    public String healthCheck() {
        return "Application is up and running!";
    }

    @GetMapping("/trade")
    public String trade() {
        tradingService.trade("BTCUSDT", "BUY", "50");
        return "traded";
    }

    @GetMapping("/asset")
    public List<String> getAsset() {
        return balanceInfoService.getBalances("USDT");
    }

}
