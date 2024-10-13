package com.nechytailo.bybit.bot.bot.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class TradeEventRequestDto {

    private String name;
    private String firstSymbol;
    private String secondSymbol;
    private String executeAt;

}
