package com.nechytailo.bybit.bot.exception;

import com.nechytailo.bybit.bot.model.ErrorType;

public class NoAccountsException extends GeneralServiceException {

    public NoAccountsException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}