package com.nechytailo.bybit.bot.execution;

import com.nechytailo.bybit.bot.model.EventStatus;
import com.nechytailo.bybit.bot.model.TradeEvent;
import com.nechytailo.bybit.bot.service.EventService;
import com.nechytailo.bybit.bot.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventExecutor {

    @Autowired
    private TradingService tradingService;

    @Autowired
    private EventService eventService;

    public void executeEvent(TradeEvent tradeEvent) {
        try {
            tradingService.trade("BTCUSDT", "BUY", "50");
            eventService.updateEventStatus(tradeEvent, EventStatus.COMPLETED);
        } catch (Exception e) {
            eventService.updateEventStatus(tradeEvent, EventStatus.FAILED);
        }
    }
}
