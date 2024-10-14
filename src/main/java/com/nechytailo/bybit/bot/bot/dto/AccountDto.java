package com.nechytailo.bybit.bot.bot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {
    private String apiKey;
    private String apiSecret; //TODO delete and add new dto
    private ProxyParamsDto proxyParams;

    @Override
    public String toString() {
        return "apiKey='" + apiKey + '\'' +
                ", proxyParams=" + proxyParams;
    }
}
