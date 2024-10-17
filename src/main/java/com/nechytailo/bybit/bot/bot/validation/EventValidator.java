package com.nechytailo.bybit.bot.bot.validation;

import com.nechytailo.bybit.bot.bot.dto.TradeEventRequestDto;
import com.nechytailo.bybit.bot.bot.exception.InvalidEventArgumentException;
import com.nechytailo.bybit.bot.model.ErrorType;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EventValidator {
    private static final String TRADE_EVENT_REGEX = "^([A-Za-z0-9]+),([A-Z]{2,8}),([A-Z]{2,8}),(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$";
    private static final Pattern TRADE_EVENT_PATTERN = Pattern.compile(TRADE_EVENT_REGEX);

    public TradeEventRequestDto processTradeEvent(String input) throws InvalidEventArgumentException {
        Matcher matcher = TRADE_EVENT_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new InvalidEventArgumentException("Invalid trade event format", ErrorType.INVALID_EVENT_PARAMETERS);
        }

        return TradeEventRequestDto.builder()
                .name(matcher.group(1))
                .firstSymbol(matcher.group(2))
                .secondSymbol(matcher.group(3))
                .executeAt(matcher.group(4))
                .build();
    }
}
