package com.nechytailo.bybit.bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class GetMarketPriceResponseDto {

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
        @JsonProperty("category")
        private String category;

        @JsonProperty("list")
        private List<MarketData> marketDataList;
    }

    @Data
    public static class MarketData {
        @JsonProperty("symbol")
        private String symbol;

        @JsonProperty("bid1Price")
        private String bid1Price;

        @JsonProperty("bid1Size")
        private String bid1Size;

        @JsonProperty("ask1Price")
        private String ask1Price;

        @JsonProperty("ask1Size")
        private String ask1Size;

        @JsonProperty("lastPrice")
        private String lastPrice;

        @JsonProperty("prevPrice24h")
        private String prevPrice24h;

        @JsonProperty("price24hPcnt")
        private String price24hPcnt;

        @JsonProperty("highPrice24h")
        private String highPrice24h;

        @JsonProperty("lowPrice24h")
        private String lowPrice24h;

        @JsonProperty("turnover24h")
        private String turnover24h;

        @JsonProperty("volume24h")
        private String volume24h;

        @JsonProperty("usdIndexPrice")
        private String usdIndexPrice;
    }
}
