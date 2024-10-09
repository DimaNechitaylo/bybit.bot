package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.entity.Account;

public interface BybitApiService {

    public void placeMarketOrder(Account account, String symbol, String side, String quantity);

    public String getCoinBalance(Account account, String token);
}