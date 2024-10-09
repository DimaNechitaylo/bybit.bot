package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.exception.NoAccountsException;

public interface TradingService {
    void trade(String symbol, String side, String quantity) throws NoAccountsException;
}
