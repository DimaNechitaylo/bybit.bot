package com.nechytailo.bybit.bot.repository;

import com.nechytailo.bybit.bot.entity.EventStatus;
import com.nechytailo.bybit.bot.entity.TradeEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<TradeEvent, Long> {
//    List<TradeEvent> findByStatusAndExecuteAtBefore(EventStatus status, LocalDateTime dateTime); //TODO delete
    List<TradeEvent> findByStatusAndExecuteAtBetween(EventStatus status, LocalDateTime start, LocalDateTime end);
    List<TradeEvent> findByUserIdAndStatusIn(Long userId, List<EventStatus> statuses);

}
