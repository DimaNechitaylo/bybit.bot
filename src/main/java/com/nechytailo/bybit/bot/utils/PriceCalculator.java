package com.nechytailo.bybit.bot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PriceCalculator {

    @Value("${price.increase.percentage}")
    private BigDecimal increasePercentage;  // Загружаем процент

    @Value("${price.decrease.percentage}")
    private BigDecimal decreasePercentage;  // Загружаем процент уменьшения

    public String getIncreasedPrice(String lastPrice) {
        BigDecimal price = new BigDecimal(lastPrice);
        BigDecimal multiplier = BigDecimal.ONE.add(increasePercentage.divide(new BigDecimal("100")));
        BigDecimal increasedPrice = price.multiply(multiplier);
        return increasedPrice.toString();
    }

    public String getDecreasedPrice(String lastPrice) {
        BigDecimal price = new BigDecimal(lastPrice);
        BigDecimal multiplier = BigDecimal.ONE.subtract(decreasePercentage.divide(new BigDecimal("100")));
        BigDecimal decreasedPrice = price.multiply(multiplier);
        return decreasedPrice.toString();
    }

    public String calculateQtyOfTargetCoinToBuy(String priceStr, String usdtAmountStr) {
        BigDecimal price = new BigDecimal(priceStr);
        BigDecimal usdtAmount = new BigDecimal(usdtAmountStr);
        BigDecimal qty = usdtAmount.divide(price, 5, BigDecimal.ROUND_DOWN); //TODO change to 0
        return qty.toString();
    }
}
