package com.nechytailo.bybit.bot.bot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {
    private String apiKey;
    private String apiSecret;
    private ProxyParamsDto proxyParams;
}
