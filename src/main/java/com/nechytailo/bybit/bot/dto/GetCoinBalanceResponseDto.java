package com.nechytailo.bybit.bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetCoinBalanceResponseDto {
    @JsonProperty("retCode")
    private int retCode;

    @JsonProperty("retMsg")
    private String retMsg;

    @JsonProperty("result")
    private Result result;

    @JsonProperty("retExtInfo")
    private RetExtInfo retExtInfo;

    @JsonProperty("time")
    private long time;

    @Data
    public static class Result {

        @JsonProperty("accountType")
        private String accountType;

        @JsonProperty("bizType")
        private int bizType;

        @JsonProperty("accountId")
        private String accountId;

        @JsonProperty("memberId")
        private String memberId;

        @JsonProperty("balance")
        private Balance balance;

        @Data
        public static class Balance {

            @JsonProperty("coin")
            private String coin;

            @JsonProperty("walletBalance")
            private String walletBalance;

            @JsonProperty("transferBalance")
            private String transferBalance;

            @JsonProperty("bonus")
            private String bonus;

            @JsonProperty("transferSafeAmount")
            private String transferSafeAmount;

            @JsonProperty("ltvTransferSafeAmount")
            private String ltvTransferSafeAmount;
        }
    }
    @Data
    public static class RetExtInfo {
    }
}
