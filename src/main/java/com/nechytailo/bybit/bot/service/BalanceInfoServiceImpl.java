package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.model.Account;
import com.nechytailo.bybit.bot.model.ProxyParams;
import com.nechytailo.bybit.bot.utils.URLs;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

//    @PostConstruct
//    public void init() {
//        loadAccounts();
//    }
//
//    public void loadAccounts() { //TODO вінести в отдельній сервис
//        //TODO Загрузка аккаунтов из файла или БД
//        // Пример добавления аккаунтов вручную для тестирования
//        accounts.put("account1", new Account("account1",
//                apiKey, apiSecret, new ProxyParams("proxyHost",
//                80, "proxyLogin", "proxyPassword")));
//        // Добавьте другие аккаунты по аналогии
//    }


    @Override
    public List<String> getBalances() {
        List<String> balances = new ArrayList<>();
        accounts.forEach((accountId, account) -> {
            Proxy proxy = null; //TODO new Proxy(Proxy.Type.HTTP, new InetSocketAddress(account.getProxyHost(), Integer.parseInt(account.getProxyPort())));
            String balanceInfo = bybitApiService.getCoinBalance(account);
            balances.add(balanceInfo);
            // Обработка баланса
            System.out.println("%%%%%%%%%%%%%%%%55 Account ID: " + accountId + " Balance Info: " + balanceInfo);
        });
        return balances;
    }


}
