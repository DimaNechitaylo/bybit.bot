package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.dto.GetBalanceResponseDto;
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
    public List<GetBalanceResponseDto> getBalances(String token) throws NoAccountsException {
        List<GetBalanceResponseDto> balances = new ArrayList<>();
        List<Account> accounts = accountService.getAllAccounts();

        accounts.forEach((account) -> {
            GetBalanceResponseDto balanceInfo = bybitApiService.getCoinBalance(account, token);
            balances.add(balanceInfo);
            LOG.info("Balance Info: {}", balanceInfo);

            delayService.doAccountGetBalancesDelay();

        });
        return balances;
    }


}
