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

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Value("${bybit.api.key0}")
    private String apiKey0;

    @Value("${bybit.api.secret0}")
    private String apiSecret0;

    @Value("${bybit.api.key1}")
    private String apiKey1;

    @Value("${bybit.api.secret1}")
    private String apiSecret1;

    private static final int REPEATS = 10;
    private static final int MAX_RETRIES = 3;
    private static final int DELAY_BETWEEN_RETRIES = 5000; // milliseconds

//    @PostConstruct
//    public void init() {
//        loadAccounts();
//    }

//    public void loadAccounts() { //TODO вінести в отдельній сервис
//        //TODO Загрузка аккаунтов из файла или БД
//        // Пример добавления аккаунтов вручную для тестирования
//        accounts.put("account1", new Account("account1",
//                apiKey0, apiSecret0, new ProxyParams("proxyHost",
//                80, "proxyLogin", "proxyPassword")));
//        accounts.put("account2", new Account("account1",
//                apiKey1, apiSecret1, new ProxyParams("proxyHost",
//                80, "proxyLogin", "proxyPassword")));
//        // Добавьте другие аккаунты по аналогии
//    }


    @Override
    public void trade(String symbol, String side, String quantity) {
        accounts.forEach((accountId, account) -> { //TODO проверка на повторение аккаунтов
            bybitApiService.placeMarketOrder(account, symbol, side, quantity);

        int delay = new Random().nextInt(pauseRange[1] - pauseRange[0]) + pauseRange[0];
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
