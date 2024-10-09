package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.exception.NoAccountsException;

import java.util.List;

public interface BalanceInfoService {

    List<String> getBalances(String token) throws NoAccountsException;
}
