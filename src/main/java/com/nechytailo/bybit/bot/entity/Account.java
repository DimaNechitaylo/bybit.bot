package com.nechytailo.bybit.bot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String apiKey;
    private String apiSecret;
    @OneToOne(cascade = CascadeType.ALL) //TODO check it
    @JoinColumn(name = "proxy_params_id")
    private ProxyParams proxyParams;
}
