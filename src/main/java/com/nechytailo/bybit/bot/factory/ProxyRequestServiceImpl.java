package com.nechytailo.bybit.bot.factory;

import com.nechytailo.bybit.bot.model.ProxyParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

@Service
public class ProxyRequestServiceImpl implements ProxyRequestService {

    @Value("{$proxy.enabled:true}")
    private boolean proxyEnabled;

    @Override
    public RestTemplate createRestTemplateWithProxy(ProxyParams proxyParams) {
        if (!proxyEnabled) return new RestTemplate();
        return createRestTemplateWithProxy(proxyParams.getProxyHost(),
                proxyParams.getProxyPort(),
                proxyParams.getProxyLogin(),
                proxyParams.getProxyPassword());
    }

    public RestTemplate createRestTemplateWithProxy(String proxyHost, int proxyPort, String proxyUser, String proxyPassword) { //TODO обрабатывать как ошибки отсутствие подключения к прокси, проверять как-то.
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                if (getRequestingHost().equals(proxyHost)) {
                    return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                }
                return null;
            }
        });
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setProxy(proxy);
        
        return new RestTemplate(factory);
    }

}
