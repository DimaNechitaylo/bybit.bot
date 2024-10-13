package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.dto.GetAccountBalanceResponseDto;
import com.nechytailo.bybit.bot.dto.GetCoinBalanceResponseDto;
import com.nechytailo.bybit.bot.entity.Account;
import com.nechytailo.bybit.bot.exception.NoAccountsException;

import java.util.List;

public interface BalanceInfoService {

    List<GetCoinBalanceResponseDto> getCoinBalances(String token) throws NoAccountsException;

    List<GetAccountBalanceResponseDto> getUsersBalances(List<Account> accounts) throws NoAccountsException;
}
