package com.nechytailo.bybit.bot.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ByBitEndpoints {

    @Value("${bybit.market.time:/v5/market/time}")
    public String systemTimeEndpoint;

    @Value("${bybit.coin.balance:/v5/asset/transfer/query-account-coin-balance}")
    public String coinBalanceEndpoint;

    @Value("${bybit.create.order:/v5/order/create}")
    public String createOrderEndpoint;

    @Value("${bybit.wallet-balance:/v5/account/wallet-balance}")
    public String accountBalancesEndpoint;

    @Value("${bybit.market-price:/v5/market/tickers}")
    public String marketPriceEndpoint;

}
