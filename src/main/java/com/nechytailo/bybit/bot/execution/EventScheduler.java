package com.nechytailo.bybit.bot.execution;

import com.nechytailo.bybit.bot.model.EventStatus;
import com.nechytailo.bybit.bot.model.TradeEvent;
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

    private Duration getLookAheadDuration() {
        return Duration.ofMinutes(lookAheadTime);
    }

    @Scheduled(fixedRate = 10000) // Проверка каждые 10 секунд
    public void scheduleUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lookAheadTime = now.plus(getLookAheadDuration());

        // Получаем события, которые должны быть выполнены в ближайшие 5 минут
        List<TradeEvent> tradeEvents = eventService.getPendingEventsWithinTimeRange(now, lookAheadTime);

        for (TradeEvent tradeEvent : tradeEvents) {
            Instant executeAtInstant = convertToInstant(tradeEvent.getExecuteAt()); // Преобразование LocalDateTime в Instant
            taskScheduler.schedule(() -> executeEvent(tradeEvent), executeAtInstant);
            eventService.updateEventStatus(tradeEvent, EventStatus.PENDING);
        }

        logEventsToSchedule(tradeEvents);
    }

    // Метод для выполнения события
    private void executeEvent(TradeEvent tradeEvent) {
        try {
            LocalDateTime now = LocalDateTime.now();
            tradingService.trade(tradeEvent.getSymbol(), tradeEvent.getSide(), tradeEvent.getQuantity());
            LOG.info("Executing event at: {}, scheduled for: {}", now, tradeEvent.getExecuteAt());
            eventService.updateEventStatus(tradeEvent, EventStatus.COMPLETED);
        } catch (Exception e) {
            LOG.error("Executing event failed at: {}, scheduled for: {}. Error message: {}, Exception:{}",
                    LocalDateTime.now(), tradeEvent.getExecuteAt(), e.getMessage(), e.getStackTrace());
            eventService.updateEventStatus(tradeEvent, EventStatus.FAILED);
        }
    }

    private Instant  convertToInstant(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    private void logEventsToSchedule(List<TradeEvent> tradeEvents){
        if (!tradeEvents.isEmpty()) {
            String eventDetails = tradeEvents.stream()
                    .map(tradeEvent -> String.format("Event[id=%d, description='%s', executeAt=%s, status=%s]",
                            tradeEvent.getId(), tradeEvent.getDescription(), tradeEvent.getExecuteAt(), tradeEvent.getStatus()))
                    .collect(Collectors.joining(", "));

            LOG.debug("Events to schedule: {}", eventDetails);
        } else {
            LOG.debug("No events to schedule at this time.");
        }
    }
}

