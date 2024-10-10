package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.bot.dto.AccountDto;
import com.nechytailo.bybit.bot.bot.dto.TradeEventRequestDto;
import com.nechytailo.bybit.bot.bot.dto.TradeEventResponseDto;
import com.nechytailo.bybit.bot.bot.exception.UserNotFoundException;
import com.nechytailo.bybit.bot.entity.EventStatus;
import com.nechytailo.bybit.bot.entity.TradeEvent;
import com.nechytailo.bybit.bot.entity.User;
import com.nechytailo.bybit.bot.exception.NoAccountsException;
import com.nechytailo.bybit.bot.exception.NoTradeEventException;
import com.nechytailo.bybit.bot.mapper.EventMapper;
import com.nechytailo.bybit.bot.model.ErrorType;
import com.nechytailo.bybit.bot.repository.EventRepository;
import com.nechytailo.bybit.bot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

//    public List<TradeEvent> getPendingEvents(LocalDateTime now) { //TODO delete
//        return eventRepository.findByStatusAndExecuteAtBefore(EventStatus.NEW, now);
//    }

    public TradeEventResponseDto addEvent(TradeEventRequestDto tradeEventRequestDto, Long userId) throws UserNotFoundException {
        TradeEvent tradeEvent = eventMapper.toEntity(tradeEventRequestDto);
        tradeEvent.setStatus(EventStatus.NEW);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found", ErrorType.USER_NOT_FOUND));
        tradeEvent.setUser(user);
        return eventMapper.toResponseDto(eventRepository.save(tradeEvent));
    }

    public List<TradeEvent> getNewEventsWithinTimeRangeByUserid(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByStatusAndExecuteAtBetween(EventStatus.NEW, start, end);
    }

    public List<TradeEventResponseDto> getEventsByUserIdWithPendingAndNewStatus(Long userId) throws NoTradeEventException {
        List<TradeEventResponseDto> responseDtoList = eventMapper.toResponseDtoList(
                eventRepository.findByUserIdAndStatusIn(userId, List.of(EventStatus.PENDING, EventStatus.NEW))
        );
        return responseDtoList.stream()
                .findAny()
                .map(a -> responseDtoList)
                .orElseThrow(() -> new NoTradeEventException("There is no events in DB", ErrorType.NO_ACCOUNTS));
    }

    public void updateEventStatus(TradeEvent tradeEvent, EventStatus status) {
        tradeEvent.setStatus(status);
        eventRepository.save(tradeEvent);
    }

}

