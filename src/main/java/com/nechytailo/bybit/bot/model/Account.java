package com.nechytailo.bybit.bot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name="accounts")
public class Account {
    @Id
    private Long id;
    private String accountHost;
    private String apiKey;
    private String apiSecret;
    @OneToOne
    private ProxyParams proxyParams;
}
