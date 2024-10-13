package com.nechytailo.bybit.bot.bot.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountBalanceDto {

    private long accountId;
    private String accountType;
    private String totalAvailableBalance;
    private List<CoinInfo> coin;

    @Data
    public static class CoinInfo {
        private String usdValue;
        private String walletBalance;
        private String coin;
    }

}
