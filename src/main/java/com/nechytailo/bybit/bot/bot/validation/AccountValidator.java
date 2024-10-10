package com.nechytailo.bybit.bot.bot.validation;

import com.nechytailo.bybit.bot.bot.dto.AccountDto;
import com.nechytailo.bybit.bot.bot.dto.ProxyParamsDto;
import com.nechytailo.bybit.bot.bot.exception.InvalidAccountArgumentException;
import com.nechytailo.bybit.bot.bot.exception.InvalidEventArgumentException;
import com.nechytailo.bybit.bot.model.ErrorType;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AccountValidator {
    private static final String ACCOUNT_REGEX = "^([a-zA-Z0-9]{16,32}),\\s([a-zA-Z0-9]{32,64}),\\s((?:\\d{1,3}\\.){3}\\d{1,3}),\\s(\\d{1,5}),\\s([a-zA-Z0-9]+),\\s([a-zA-Z0-9]+)$";
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile(ACCOUNT_REGEX);

    public AccountDto processAccount(String input) throws InvalidAccountArgumentException {
        Matcher matcher = ACCOUNT_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new InvalidAccountArgumentException("Invalid acoount format", ErrorType.INVALID_ACCOUNT_PARAMETERS);
        }
        String apiKey = matcher.group(1);
        String apiSecret = matcher.group(2);
        String proxyHost = matcher.group(3);
        int proxyPort = Integer.parseInt(matcher.group(4));
        String proxyLogin = matcher.group(5);
        String proxyPassword = matcher.group(6);

        return AccountDto.builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .proxyParams(ProxyParamsDto.builder()
                        .proxyHost(proxyHost)
                        .proxyPort(proxyPort)
                        .proxyLogin(proxyLogin)
                        .proxyPassword(proxyPassword)
                        .build())
                .build();
    }
}