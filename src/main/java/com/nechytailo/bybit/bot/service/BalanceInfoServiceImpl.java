package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.model.Account;
import com.nechytailo.bybit.bot.utils.URLs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class BalanceInfoServiceImpl implements BalanceInfoService {
    private static final Logger LOG = LoggerFactory.getLogger(BalanceInfoServiceImpl.class);
    private static final long RECV_WINDOW = 20000;

    @Value("${bybit.api.token}")
    private String token;

//    @Value("${bybit.api.threshold}")
//    private Double threshold;

    @Value("${bybit.api.accountType}")
    private String accountType;

    @Value("${bybit.api.key0}")
    private String apiKey;

    @Value("${bybit.api.secret0}")
    private String apiSecret;

//    @Value("${bybit.api.accountDelayRangeMin}")
//    private int delayRangeMin;
//
//    @Value("${bybit.api.accountDelayRangeMax}")
//    private int delayRangeMax;

    @Autowired
    private BybitApiService bybitApiService;

    @Autowired
    private URLs urls;

    @Autowired
    private AccountService accountService;

    @Override
    public List<String> getBalances(String token) {
        List<String> balances = new ArrayList<>();
        List<Account> accounts = accountService.getAllAccounts();

        accounts.forEach((account) -> {
            String balanceInfo = bybitApiService.getCoinBalance(account, token);
            balances.add(balanceInfo);
            LOG.info("Balance Info: {}", balanceInfo);
        });
        return balances;
    }


}
