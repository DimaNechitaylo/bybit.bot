package com.nechytailo.bybit.bot.controller;

import com.nechytailo.bybit.bot.entity.Account;
import com.nechytailo.bybit.bot.exception.NoAccountsException;
import com.nechytailo.bybit.bot.service.BalanceInfoService;
import com.nechytailo.bybit.bot.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> trade() {
        try {
            tradingService.trade("BTCUSDT", "BUY", "50");
        } catch (NoAccountsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No accounts available");
        }
        return ResponseEntity.ok("traded");
    }

    @GetMapping("/asset")
    public ResponseEntity<?> getAsset() {
        List<String> assets;
        try {
            assets = balanceInfoService.getBalances("USDT");
        } catch (NoAccountsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No accounts available");
        }
        return ResponseEntity.ok(assets);
    }

}
