package com.nechytailo.bybit.bot.utils;

import java.util.UUID;

public class UniqueOrderLinkIdGenerator {
    public static String createUniqueOrderLinkId() {
        return UUID.randomUUID().toString();
    }
}
