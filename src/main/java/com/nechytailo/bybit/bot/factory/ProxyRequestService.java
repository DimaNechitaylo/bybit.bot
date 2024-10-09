package com.nechytailo.bybit.bot.factory;

import com.nechytailo.bybit.bot.entity.ProxyParams;
import org.springframework.web.client.RestTemplate;

public interface ProxyRequestService {
    public RestTemplate createRestTemplateWithProxy(ProxyParams proxyParams);
}
