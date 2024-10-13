package com.nechytailo.bybit.bot.execution;

import com.nechytailo.bybit.bot.entity.EventStatus;
import com.nechytailo.bybit.bot.entity.TradeEvent;
import com.nechytailo.bybit.bot.service.EventService;
import com.nechytailo.bybit.bot.service.TradingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(EventScheduler.class);

    @Value("${look.ahead.time:10}")
    private long lookAheadTime;

    @Autowired
    private EventService eventService;

    @Autowired
    private TradingService tradingService;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private EventExecutor eventExecutor;

    private Duration getLookAheadDuration() {
        return Duration.ofMinutes(lookAheadTime);
    }

    @Scheduled(fixedRate = 100000) // 100 sec
    public void scheduleUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lookAheadTime = now.plus(getLookAheadDuration());

        List<TradeEvent> tradeEvents = eventService.getNewEventsWithinTimeRangeByUserid(now, lookAheadTime);

        for (TradeEvent tradeEvent : tradeEvents) {
            Instant executeAtInstant = convertToInstant(tradeEvent.getExecuteAt());
            taskScheduler.schedule(() -> eventExecutor.executeTradeEvent(tradeEvent), executeAtInstant);
            eventService.updateEventStatus(tradeEvent, EventStatus.PENDING);
        }

        logEventsToSchedule(tradeEvents);
    }

    private Instant convertToInstant(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    private void logEventsToSchedule(List<TradeEvent> tradeEvents){
        if (!tradeEvents.isEmpty()) {
            String eventDetails = tradeEvents.stream()
                    .map(tradeEvent -> String.format("Event[id=%d, name='%s', executeAt=%s, status=%s]",
                            tradeEvent.getId(), tradeEvent.getName(), tradeEvent.getExecuteAt(), tradeEvent.getStatus()))
                    .collect(Collectors.joining(", "));

            LOG.debug("Events to schedule: {}", eventDetails);
        } else {
            LOG.debug("No events to schedule at this time.");
        }
    }
}

