package com.nechytailo.bybit.bot.exception;


import com.nechytailo.bybit.bot.model.ErrorType;
import lombok.Getter;

@Getter
public class GeneralServiceException extends Exception implements ServiceException {

    private final ErrorType errorType;

    public GeneralServiceException(Throwable cause) {
        super(cause);
        this.errorType = ErrorType.UNKNOWN_ERROR;
    }

    public GeneralServiceException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public GeneralServiceException(Throwable cause, ErrorType errorType) {
        super(cause);
        this.errorType = errorType;
    }

    public GeneralServiceException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

}
