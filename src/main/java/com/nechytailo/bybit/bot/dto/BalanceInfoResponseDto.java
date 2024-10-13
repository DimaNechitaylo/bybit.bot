package com.nechytailo.bybit.bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BalanceInfoResponseDto {
    @JsonProperty("accountType")
    private String accountType;

    @JsonProperty("totalAvailableBalance")
    private String totalAvailableBalance;

    @JsonProperty("coin")
    private List<CoinInfo> coin;

    @Data
    public static class CoinInfo {

        @JsonProperty("usdValue")
        private String usdValue;

        @JsonProperty("walletBalance")
        private String walletBalance;

        @JsonProperty("coin")
        private String coin;
    }
}
