package com.nechytailo.bybit.bot.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Service
public class SignServiceImpl implements SignService { //TODO refactor

    @Value("${bybit.recv_window:20000}")
    private long recvWindow;

    @Override
    public String signGet(String apiKey, String apiSecret, String timestamp, Map<String, Object> params) {
        StringBuilder sb = genQueryStr(params);
        String queryStr = timestamp + apiKey + recvWindow + sb;

        Mac sha256_HMAC = null;
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        SecretKeySpec secret_key = new SecretKeySpec(apiSecret.getBytes(), "HmacSHA256");
        try {
            sha256_HMAC.init(secret_key);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return bytesToHex(sha256_HMAC.doFinal(queryStr.getBytes()));

    }


    @Override
    public String signPost(String apiKey, String apiSecret, String timestamp, Map<String, Object> params) {
        Mac sha256_HMAC = null;
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");

            SecretKeySpec secret_key = new SecretKeySpec(apiSecret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        Gson gson = new Gson();
        String paramJson = gson.toJson(params); //TODO Используем Gson для сериализации в JSON
        String sb = timestamp + apiKey + recvWindow + paramJson;

        return bytesToHex(sha256_HMAC.doFinal(sb.getBytes()));

    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static StringBuilder genQueryStr(Map<String, Object> map) {
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        StringBuilder sb = new StringBuilder();
        while (iter.hasNext()) {
            String key = iter.next();
            sb.append(key)
                    .append("=")
                    .append(map.get(key))
                    .append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb;
    }

}
