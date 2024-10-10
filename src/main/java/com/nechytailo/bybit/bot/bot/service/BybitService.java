package com.nechytailo.bybit.bot.bot.service;

import com.nechytailo.bybit.bot.bot.dto.AccountDto;
import com.nechytailo.bybit.bot.bot.dto.TradeEventRequestDto;
import com.nechytailo.bybit.bot.bot.dto.TradeEventResponseDto;
import com.nechytailo.bybit.bot.bot.exception.UserNotFoundException;
import com.nechytailo.bybit.bot.exception.NoAccountsException;
import com.nechytailo.bybit.bot.exception.NoTradeEventException;

import java.util.Collection;
import java.util.List;

public interface BybitService {

    TradeEventResponseDto addEvent(TradeEventRequestDto tradeEventRequestDto, Long userId) throws UserNotFoundException;

    List<TradeEventResponseDto> getUserEvents(Long userid) throws NoTradeEventException;

    AccountDto addAccount(AccountDto accountDto, Long userId) throws UserNotFoundException;

    List<AccountDto> getUserAccounts(Long userId) throws NoAccountsException;
}
