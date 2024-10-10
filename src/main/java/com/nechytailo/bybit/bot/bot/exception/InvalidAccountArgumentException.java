package com.nechytailo.bybit.bot.bot.exception;

import com.nechytailo.bybit.bot.exception.GeneralServiceException;
import com.nechytailo.bybit.bot.model.ErrorType;

public class InvalidAccountArgumentException extends GeneralServiceException {

    public InvalidAccountArgumentException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
