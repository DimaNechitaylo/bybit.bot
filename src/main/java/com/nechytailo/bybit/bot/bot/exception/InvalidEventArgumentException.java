package com.nechytailo.bybit.bot.bot.exception;

import com.nechytailo.bybit.bot.exception.GeneralServiceException;
import com.nechytailo.bybit.bot.model.ErrorType;

public class InvalidEventArgumentException extends GeneralServiceException {

    public InvalidEventArgumentException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
