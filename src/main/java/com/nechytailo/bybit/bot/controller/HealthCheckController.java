package com.nechytailo.bybit.bot.controller;

import com.nechytailo.bybit.bot.dto.GetAccountBalanceResponseDto;
import com.nechytailo.bybit.bot.dto.GetCoinBalanceResponseDto;
import com.nechytailo.bybit.bot.exception.NoAccountsException;
import com.nechytailo.bybit.bot.service.BalanceInfoService;
import com.nechytailo.bybit.bot.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

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

    @GetMapping("/buy")
    public ResponseEntity<?> buy() {
        try {
            tradingService.buy("BTCUSDT", "SELL", "0.003");
        } catch (NoAccountsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No accounts available");
        }
        return ResponseEntity.ok("traded");
    }

    @GetMapping("/trade")
    public ResponseEntity<?> trade() {
        try {
            tradingService.trade("BTC", "USDT");
        } catch (NoAccountsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No accounts available");
        }
        return ResponseEntity.ok("traded");
    }

    @GetMapping("/asset")
    public ResponseEntity<?> getAsset() {
        String assets;
        try {
            assets = balanceInfoService.getCoinBalances("USDT").stream()
                    .map(GetCoinBalanceResponseDto::toString)
                    .collect(Collectors.joining("<br><br>"));;
        } catch (NoAccountsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No accounts available");
        }
        return ResponseEntity.ok(assets);
    }
//    @GetMapping("/wallet-balance")
//    public ResponseEntity<?> getWalletBalance() {
//        String assets;
//        try {
//            assets = balanceInfoService.getBalances().stream()
//                    .map(GetAccountBalanceResponseDto::toString)
//                    .collect(Collectors.joining("<br><br>"));;
//        } catch (NoAccountsException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("No accounts available");
//        }
//        return ResponseEntity.ok(assets);
//    }

}
