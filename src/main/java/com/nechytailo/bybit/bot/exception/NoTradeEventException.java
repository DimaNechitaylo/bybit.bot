package com.nechytailo.bybit.bot.exception;

import com.nechytailo.bybit.bot.model.ErrorType;

public class NoTradeEventException extends GeneralServiceException {

    public NoTradeEventException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}