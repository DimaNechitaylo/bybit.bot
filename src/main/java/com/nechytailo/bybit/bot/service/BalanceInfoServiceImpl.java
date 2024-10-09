package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.entity.Account;
import com.nechytailo.bybit.bot.exception.NoAccountsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class BalanceInfoServiceImpl implements BalanceInfoService {
    private static final Logger LOG = LoggerFactory.getLogger(BalanceInfoServiceImpl.class);

    @Value("${trading.pause-range}")
    private int[] pauseRange;

    @Autowired
    private BybitApiService bybitApiService;

    @Autowired
    private AccountService accountService;

    @Override
    public List<String> getBalances(String token) throws NoAccountsException {
        Random random = new Random();
        List<String> balances = new ArrayList<>();
        List<Account> accounts = accountService.getAllAccounts();

        accounts.forEach((account) -> {
            String balanceInfo = bybitApiService.getCoinBalance(account, token);
            balances.add(balanceInfo);
            LOG.info("Balance Info: {}", balanceInfo);

            int delay = random.nextInt(pauseRange[1] - pauseRange[0]) + pauseRange[0];
            LOG.info("Selected delay: {} seconds%n", delay);
            try {
                TimeUnit.SECONDS.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted during sleep", e);
            }
        });
        return balances;
    }


}
