package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.dto.GetAccountBalanceResponseDto;
import com.nechytailo.bybit.bot.dto.GetCoinBalanceResponseDto;
import com.nechytailo.bybit.bot.dto.GetMarketPriceResponseDto;
import com.nechytailo.bybit.bot.entity.Account;

public interface BybitApiService {

    void placeMarketOrderWithQty(Account account, String symbol, String side, String quantity);

    void placeLimitOrderWithQty(Account account, String symbol, String side, String quantity, String price);

    void instantTradeMarket(Account account, String coinToBuy, String coinForBuy);

    void instantTradeLimit(Account account, String coinToBuy, String coinForBuy);

    GetCoinBalanceResponseDto getCoinBalance(Account account, String token);

    GetMarketPriceResponseDto getMarketPrice(Account account, String symbol);

    GetAccountBalanceResponseDto getAccountBalances(Account account);
}