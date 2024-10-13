package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.entity.Account;
import com.nechytailo.bybit.bot.exception.NoAccountsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;

import java.util.*;

@Service
public class TradingServiceImpl implements TradingService{

    private static final Logger LOG = LoggerFactory.getLogger(TradingServiceImpl.class);

    @Autowired
    private BybitApiService bybitApiService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private DelayService delayService;

    @Override
    public void buy(String symbol, String side, String quantity) throws NoAccountsException {
        List<Account> accounts = accountService.getAllAccounts();

        accounts.forEach((account) -> { //TODO проверка на повторение аккаунтов. (может юзaть Set)
            bybitApiService.placeMarketOrderWithQty(account, symbol, side, quantity);

            delayService.doAccountTradeDelay();
        });

    }

    @Override
    public void trade(String coinToBuy, String coinForBuy) throws NoAccountsException {
        List<Account> accounts = accountService.getAllAccounts();

        accounts.forEach((account) -> { //TODO проверка на повторение аккаунтов. (может юзaть Set)
            bybitApiService.instantTrade(account, coinToBuy, coinForBuy);
            delayService.doAccountTradeDelay();
        });

    }

}
