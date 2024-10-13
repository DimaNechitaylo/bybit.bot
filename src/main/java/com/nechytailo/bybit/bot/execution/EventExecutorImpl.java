package com.nechytailo.bybit.bot.execution;

import com.nechytailo.bybit.bot.entity.EventStatus;
import com.nechytailo.bybit.bot.entity.TradeEvent;
import com.nechytailo.bybit.bot.service.EventService;
import com.nechytailo.bybit.bot.service.TradingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventExecutorImpl implements EventExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(EventExecutorImpl.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private TradingService tradingService;

    @Override
    public void executeTradeEvent(TradeEvent tradeEvent) {
        try {
            tradingService.trade(tradeEvent.getFirstSymbol(), tradeEvent.getSecondSymbol());
            LocalDateTime now = LocalDateTime.now();
            LOG.info("Executing event at: {}, scheduled for: {}", now, tradeEvent.getExecuteAt());
            eventService.updateEventStatus(tradeEvent, EventStatus.COMPLETED);
        } catch (Exception e) {
            LOG.error("Executing event failed at: {}, scheduled for: {}. Error message: {}, Exception:{}",
                    LocalDateTime.now(), tradeEvent.getExecuteAt(), e.getMessage(), e.getStackTrace());
            eventService.updateEventStatus(tradeEvent, EventStatus.FAILED);
        }
    }

}
