package com.nechytailo.bybit.bot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorType {
    INTERNAL_ERROR(501),
    UNKNOWN_ERROR(502),
    NO_ACCOUNTS(503),
    INVALID_EVENT_PARAMETERS(504),
    INVALID_ACCOUNT_PARAMETERS(505),
    USER_NOT_FOUND(506);

    private int errorCode;
}
