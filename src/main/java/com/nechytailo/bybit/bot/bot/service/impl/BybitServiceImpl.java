package com.nechytailo.bybit.bot.bot.service.impl;

import com.nechytailo.bybit.bot.bot.dto.AccountDto;
import com.nechytailo.bybit.bot.bot.dto.TradeEventRequestDto;
import com.nechytailo.bybit.bot.bot.dto.TradeEventResponseDto;
import com.nechytailo.bybit.bot.bot.exception.UserNotFoundException;
import com.nechytailo.bybit.bot.bot.service.BybitService;
import com.nechytailo.bybit.bot.exception.NoAccountsException;
import com.nechytailo.bybit.bot.exception.NoTradeEventException;
import com.nechytailo.bybit.bot.service.AccountService;
import com.nechytailo.bybit.bot.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BybitServiceImpl implements BybitService {
    private static final Logger LOG = LoggerFactory.getLogger(BybitServiceImpl.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private AccountService accountService;


    @Override
    public TradeEventResponseDto addEvent(TradeEventRequestDto tradeEventRequestDto, Long userId) throws UserNotFoundException {
        LOG.debug("BybitServiceImpl.addEvent with data: {}, for userId: {}.", tradeEventRequestDto, userId);
        return eventService.addEvent(tradeEventRequestDto, userId);
    }

    @Override
    public List<TradeEventResponseDto> getUserEvents(Long userId) throws NoTradeEventException {
        LOG.debug("BybitServiceImpl.getUserEvents with userid: {}", userId);
        return eventService.getEventsByUserIdWithPendingAndNewStatus(userId);
    }

    @Override
    public AccountDto addAccount(AccountDto accountDto, Long userId) throws UserNotFoundException {
        LOG.debug("BybitServiceImpl.addAccount with data: {}, for userId: {}.", accountDto, userId);
        return accountService.addAccount(accountDto, userId);
    }

    @Override
    public List<AccountDto> getUserAccounts(Long userId) throws NoAccountsException {
        LOG.debug("BybitServiceImpl.getUserAccounts with userid: {}", userId);
        return accountService.getAccountsByUserId(userId);
    }


}
