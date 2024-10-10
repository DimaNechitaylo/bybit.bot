package com.nechytailo.bybit.bot.bot.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ProxyParamsDto {

    private String proxyHost;
    private int proxyPort;
    private String proxyLogin;
    private String proxyPassword;

}
