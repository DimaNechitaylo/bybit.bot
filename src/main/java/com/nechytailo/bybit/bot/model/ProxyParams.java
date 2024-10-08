package com.nechytailo.bybit.bot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name="proxy_params")
public class ProxyParams {
    @Id
    private Long id;
    private String proxyHost;
    private int proxyPort;
    private String proxyLogin;
    private String proxyPassword;
}
