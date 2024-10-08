package com.nechytailo.bybit.bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TimeResultDto {
    @JsonProperty("timeSecond")
    private String timeSecond;

    @JsonProperty("timeNano")
    private String timeNano;
}