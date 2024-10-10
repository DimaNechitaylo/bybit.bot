package com.nechytailo.bybit.bot.exception;


import com.nechytailo.bybit.bot.model.ErrorType;

@FunctionalInterface
public interface ServiceException {

    ErrorType getErrorType();
}
