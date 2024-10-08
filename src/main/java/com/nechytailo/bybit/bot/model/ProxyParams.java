package com.nechytailo.bybit.bot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="proxy_params")
public class ProxyParams {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String proxyHost;
    private int proxyPort;
    private String proxyLogin;
    private String proxyPassword;
}
