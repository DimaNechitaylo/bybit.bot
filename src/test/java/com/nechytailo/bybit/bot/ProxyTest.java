package com.nechytailo.bybit.bot;

import org.junit.jupiter.api.Test;

public class ProxyTest {

    @Test
    public void testThroughyHttpbin() {
        String apiUrl = "https://httpbin.org/ip";  // API для получения IP

        // Настроим прокси
        HttpHost proxy = new HttpHost("proxy_ip", 8080);  // Замените на ваш прокси
        HttpClient client = HttpClients.custom().setProxy(proxy).build();

        // Создаем RestTemplate с использованием прокси
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(client));

        // Отправляем запрос
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, HttpEntity.EMPTY, String.class);

        // Выводим IP адрес через прокси
        System.out.println("IP через прокси: " + response.getBody());
    }
}
