package com.nechytailo.bybit.bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ServerTimeDto {
    @JsonProperty("retCode")
    private int retCode;

    @JsonProperty("retMsg")
    private String retMsg;

    @JsonProperty("result")
    private TimeResultDto timeResultDto;

    @JsonProperty("retExtInfo")
    private Object retExtInfo; //TODO Можно заменить на конкретный класс, если известно

    @JsonProperty("time")
    private long time;
}
