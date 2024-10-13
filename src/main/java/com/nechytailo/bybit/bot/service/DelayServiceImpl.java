package com.nechytailo.bybit.bot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class DelayServiceImpl implements DelayService {

    private static final Logger LOG = LoggerFactory.getLogger(DelayServiceImpl.class);

    @Value("${trading.trade-pause-range:500,1500}")
    private int[] pauseTradeRange;

    @Value("${trading.get-balance-pause-range:100,400}")
    private int[] pauseGetBalanceRange;

    @Value("${trading.hold-range:1500,2300}")
    private int[] holdRange;


    private Random random = new Random();

    @Override
    public void doAccountTradeDelay() {
        int delay = random.nextInt(pauseTradeRange[1] - pauseTradeRange[0]) + pauseTradeRange[0];
        LOG.info("Selected delay between accounts during the trade: {} seconds%n", delay);
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted during sleep", e);
        }
    }

    @Override
    public void doAccountGetBalancesDelay() {
        int delay = random.nextInt(pauseGetBalanceRange[1] - pauseGetBalanceRange[0]) + pauseGetBalanceRange[0];
        LOG.info("Selected delay between accounts during the retrieving balances: {} seconds%n", delay);
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted during sleep", e);
        }
    }

    @Override
    public void doHoldDelay() {
        int delay = random.nextInt(holdRange[1] - holdRange[0]) + holdRange[0];
        LOG.info("Selected hold delay: {} seconds%n", delay);
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted during sleep", e);
        }
    }
}
