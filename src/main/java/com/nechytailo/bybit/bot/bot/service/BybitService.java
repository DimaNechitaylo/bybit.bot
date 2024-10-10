package com.nechytailo.bybit.bot.bot.service;

import com.nechytailo.bybit.bot.bot.dto.TradeEventRequestDto;
import com.nechytailo.bybit.bot.bot.dto.TradeEventResponseDto;

public interface BybitService {

    public TradeEventResponseDto addEvent(TradeEventRequestDto tradeEventRequestDto);

}
