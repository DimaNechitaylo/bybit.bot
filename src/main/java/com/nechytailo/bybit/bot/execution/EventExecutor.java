package com.nechytailo.bybit.bot.execution;

import com.nechytailo.bybit.bot.entity.TradeEvent;

public interface EventExecutor {
    public void executeTradeEvent(TradeEvent tradeEvent);
}
