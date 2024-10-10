package com.nechytailo.bybit.bot.bot.service.impl;

import com.nechytailo.bybit.bot.bot.dto.TradeEventRequestDto;
import com.nechytailo.bybit.bot.bot.dto.TradeEventResponseDto;
import com.nechytailo.bybit.bot.bot.service.BybitService;
import com.nechytailo.bybit.bot.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BybitServiceImpl implements BybitService {
    private static final Logger LOG = LoggerFactory.getLogger(BybitServiceImpl.class);

    @Autowired
    private EventService eventService;


    @Override
    public TradeEventResponseDto addEvent(TradeEventRequestDto tradeEventRequestDto) {
        LOG.debug("BybitServiceImpl.addEvent with data: {}", tradeEventRequestDto);
        return eventService.addEvent(tradeEventRequestDto);
    }

}
