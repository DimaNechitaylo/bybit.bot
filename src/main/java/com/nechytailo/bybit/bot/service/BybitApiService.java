package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.dto.GetBalanceResponseDto;
import com.nechytailo.bybit.bot.entity.Account;

public interface BybitApiService {

    public void placeMarketOrderWithQty(Account account, String symbol, String side, String quantity);

    void instantTrade(Account account, String coinToBuy, String coinForBuy);

    public GetBalanceResponseDto getCoinBalance(Account account, String token);
}