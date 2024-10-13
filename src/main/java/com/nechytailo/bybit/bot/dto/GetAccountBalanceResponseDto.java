package com.nechytailo.bybit.bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetAccountBalanceResponseDto {

    @JsonProperty("retCode")
    private int retCode;

    @JsonProperty("retMsg")
    private String retMsg;

    @JsonProperty("result")
    private Result result;

    @JsonProperty("retExtInfo")
    private Object retExtInfo;

    @JsonProperty("time")
    private long time;

    @Data
    public static class Result {

        @JsonProperty("list")
        private List<BalanceInfoResponseDto> balanceInfoResponseDtos;
    }

}
