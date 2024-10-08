package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.model.EventStatus;
import com.nechytailo.bybit.bot.model.TradeEvent;
import com.nechytailo.bybit.bot.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<TradeEvent> getPendingEvents(LocalDateTime now) { //TODO delete
        return eventRepository.findByStatusAndExecuteAtBefore(EventStatus.NEW, now);
    }

    public List<TradeEvent> getPendingEventsWithinTimeRange(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByStatusAndExecuteAtBetween(EventStatus.NEW, start, end);
    }

    public void updateEventStatus(TradeEvent tradeEvent, EventStatus status) {
        tradeEvent.setStatus(status);
        eventRepository.save(tradeEvent);
    }
}

