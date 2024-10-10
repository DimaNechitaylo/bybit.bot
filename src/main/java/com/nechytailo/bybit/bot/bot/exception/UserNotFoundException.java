package com.nechytailo.bybit.bot.bot.exception;

import com.nechytailo.bybit.bot.exception.GeneralServiceException;
import com.nechytailo.bybit.bot.model.ErrorType;

public class UserNotFoundException extends GeneralServiceException {

    public UserNotFoundException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}