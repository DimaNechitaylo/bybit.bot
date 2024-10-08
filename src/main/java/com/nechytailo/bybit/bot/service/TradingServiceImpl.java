package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.model.Account;
import com.nechytailo.bybit.bot.model.ProxyParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class TradingServiceImpl implements TradingService{

    private static final Logger LOG = LoggerFactory.getLogger(TradingServiceImpl.class);

    private final RestTemplate restTemplate = new RestTemplate();


    @Value("${trading.pause-range}")
    private int[] pauseRange;

    @Autowired
    private BybitApiService bybitApiService;

    @Autowired
    private AccountService accountService;

    @Override
    public void trade(String symbol, String side, String quantity) {
        Random random = new Random();
        List<Account> accounts = accountService.getAllAccounts();

        accounts.forEach((account) -> { //TODO проверка на повторение аккаунтов. (может юзaть Set)
            bybitApiService.placeMarketOrder(account, symbol, side, quantity);

            int delay = random.nextInt(pauseRange[1] - pauseRange[0]) + pauseRange[0];
            LOG.info("Selected delay: {} seconds%n", delay);
            try {
                TimeUnit.SECONDS.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted during sleep", e);
            }
        });

    }

}
