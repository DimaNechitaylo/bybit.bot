package com.nechytailo.bybit.bot.bot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TradeEventResponseDto {

    private String description;
    private String symbol;
    private String side;
    private String quantity;
    private String executeAt;
    private String status;

}
