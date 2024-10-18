package com.nechytailo.bybit.bot.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class Rounder {

    public static String roundToDecimal0(String qty) {
        BigDecimal number = new BigDecimal(qty);
        BigDecimal roundedNumber = number.setScale(0, RoundingMode.DOWN);
        return roundedNumber.toPlainString();
    }

    public static String roundToDecimal5(String qty) {
        BigDecimal number = new BigDecimal(qty);
        BigDecimal roundedNumber = number.setScale(5, RoundingMode.DOWN);
        return roundedNumber.toPlainString();
    }

    public static String roundToDecimal(String qty) {
        BigDecimal number = new BigDecimal(qty);
        BigDecimal roundedNumber;
        if (number.compareTo(BigDecimal.ONE) > 0) {
            roundedNumber = number.setScale(0, RoundingMode.DOWN);
        } else {
            roundedNumber = number.setScale(5, RoundingMode.DOWN);
        }
        return roundedNumber.toPlainString();
    }

}
