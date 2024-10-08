package com.nechytailo.bybit.bot.service;

public interface TradingService {
    void trade(String symbol, String side, String quantity);
}
