package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.model.Account;

import java.util.List;

public interface AccountService {
    public List<Account> getAllAccounts();

    Account addAccount(Account account);
}
