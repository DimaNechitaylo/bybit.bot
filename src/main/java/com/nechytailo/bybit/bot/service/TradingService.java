package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.exception.NoAccountsException;

public interface TradingService {
    void buy(String symbol, String side, String quantity) throws NoAccountsException;

    void tradeMarket(String coinToBuy, String coinForBuy) throws NoAccountsException;

    void tradeLimit(String coinToBuy, String coinForBuy) throws NoAccountsException;
}
