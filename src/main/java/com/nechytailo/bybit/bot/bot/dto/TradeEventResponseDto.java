package com.nechytailo.bybit.bot.bot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TradeEventResponseDto {

    private String name;
    private String firstSymbol;
    private String secondSymbol;
    private String executeAt;
    private String status;

    @Override
    public String toString() {
        return String.format("Event %s, symbol=%s%s, executeAt=%s, status=%s",
                name,
                firstSymbol,
                secondSymbol,
                executeAt,
                status);
    }
}
