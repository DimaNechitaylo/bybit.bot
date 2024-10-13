package com.nechytailo.bybit.bot.service;

public interface DelayService {

    void doAccountTradeDelay();

    void doAccountGetBalancesDelay();

    public void doHoldDelay();

}
