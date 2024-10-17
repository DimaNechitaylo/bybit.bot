package com.nechytailo.bybit.bot.execution;

import com.nechytailo.bybit.bot.entity.EventStatus;
import com.nechytailo.bybit.bot.entity.TradeEvent;
import com.nechytailo.bybit.bot.service.EventService;
import com.nechytailo.bybit.bot.service.TradingService;
import com.nechytailo.bybit.bot.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class EventExecutorImpl implements EventExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(EventExecutorImpl.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private TradingService tradingService;

    @Value("${trading.order.type:Market}")
    private String orderType;


    @Override
    public void executeTradeEvent(TradeEvent tradeEvent) {
        try {
            if (Constants.ORDER_TYPE_LIMIT.equalsIgnoreCase(orderType)) {
                tradingService.tradeLimit(tradeEvent.getFirstSymbol(), tradeEvent.getSecondSymbol());
            } else {
                tradingService.tradeMarket(tradeEvent.getFirstSymbol(), tradeEvent.getSecondSymbol());
            }
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
