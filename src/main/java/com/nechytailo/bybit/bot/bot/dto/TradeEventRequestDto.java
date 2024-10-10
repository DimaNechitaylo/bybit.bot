package com.nechytailo.bybit.bot.bot.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class TradeEventRequestDto {

    private String description;
    private String symbol;
    private String side;
    private String quantity;
    private String executeAt;

}
