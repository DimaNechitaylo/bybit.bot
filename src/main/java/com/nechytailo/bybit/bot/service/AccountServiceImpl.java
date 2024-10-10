package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.bot.dto.AccountDto;
import com.nechytailo.bybit.bot.entity.Account;
import com.nechytailo.bybit.bot.exception.NoAccountsException;
import com.nechytailo.bybit.bot.mapper.AccountMapper;
import com.nechytailo.bybit.bot.model.ErrorType;
import com.nechytailo.bybit.bot.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public List<Account> getAllAccounts() throws NoAccountsException {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .findAny()
                .map(a -> accounts)
                .orElseThrow(() -> new NoAccountsException("There is no accounts in DB", ErrorType.NO_ACCOUNTS));
    }

    @Override
    public AccountDto addAccount(AccountDto accountDto) {
        Account account = accountMapper.toEntity(accountDto);
        return accountMapper.toDto(accountRepository.save(account));
    }
}
