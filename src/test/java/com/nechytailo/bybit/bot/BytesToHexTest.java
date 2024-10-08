package com.nechytailo.bybit.bot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BytesToHexTest {


    private static String bytesToHex1(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String bytesToHex2(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Test
    public void testBytesToHexMethods() {
        byte[] testBytes1 = {0x12, 0x34, (byte) 0xab, (byte) 0xcd};
        byte[] testBytes2 = {(byte) 0xff, 0x00, 0x0a, 0x1b};

        // Сравниваем оба метода на одних и тех же данных
        assertEquals(bytesToHex1(testBytes1), bytesToHex2(testBytes1));
        assertEquals(bytesToHex1(testBytes2), bytesToHex2(testBytes2));
    }


}
