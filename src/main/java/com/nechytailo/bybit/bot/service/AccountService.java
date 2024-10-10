package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.bot.dto.AccountDto;
import com.nechytailo.bybit.bot.entity.Account;
import com.nechytailo.bybit.bot.exception.NoAccountsException;

import java.util.List;

public interface AccountService {
    public List<Account> getAllAccounts() throws NoAccountsException;

    AccountDto addAccount(AccountDto account);
}
