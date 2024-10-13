package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.dto.GetAccountBalanceResponseDto;
import com.nechytailo.bybit.bot.dto.GetCoinBalanceResponseDto;
import com.nechytailo.bybit.bot.entity.Account;
import com.nechytailo.bybit.bot.exception.NoAccountsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class BalanceInfoServiceImpl implements BalanceInfoService {
    private static final Logger LOG = LoggerFactory.getLogger(BalanceInfoServiceImpl.class);

    @Autowired
    private DelayService delayService;

    @Autowired
    private BybitApiService bybitApiService;

    @Autowired
    private AccountService accountService;

    @Override
    public List<GetCoinBalanceResponseDto> getCoinBalances(String token) throws NoAccountsException {
        List<GetCoinBalanceResponseDto> balances = new ArrayList<>();
        List<Account> accounts = accountService.getAllAccounts();

        accounts.forEach((account) -> {
            GetCoinBalanceResponseDto balanceInfo = bybitApiService.getCoinBalance(account, token);
            balances.add(balanceInfo);
            LOG.info("Balance Info: {}", balanceInfo);

            delayService.doAccountGetBalancesDelay();

        });
        return balances;
    }

    @Override
    public List<GetAccountBalanceResponseDto> getUsersBalances(List<Account> accounts) throws NoAccountsException {
        List<GetAccountBalanceResponseDto> balances = new ArrayList<>();

        accounts.forEach((account) -> {
            GetAccountBalanceResponseDto balanceInfo = bybitApiService.getAccountBalances(account);
            balances.add(balanceInfo);
            LOG.info("Balance Info: {}", balanceInfo);

            delayService.doAccountGetBalancesDelay();

        });
        return balances;
    }


}
