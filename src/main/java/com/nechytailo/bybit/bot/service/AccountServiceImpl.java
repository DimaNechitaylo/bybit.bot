package com.nechytailo.bybit.bot.service;

import com.nechytailo.bybit.bot.bot.dto.AccountDto;
import com.nechytailo.bybit.bot.bot.exception.UserNotFoundException;
import com.nechytailo.bybit.bot.entity.Account;
import com.nechytailo.bybit.bot.entity.EventStatus;
import com.nechytailo.bybit.bot.entity.User;
import com.nechytailo.bybit.bot.exception.NoAccountsException;
import com.nechytailo.bybit.bot.mapper.AccountMapper;
import com.nechytailo.bybit.bot.model.ErrorType;
import com.nechytailo.bybit.bot.repository.AccountRepository;
import com.nechytailo.bybit.bot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Account> getAllAccounts() throws NoAccountsException {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .findAny()
                .map(a -> accounts)
                .orElseThrow(() -> new NoAccountsException("There is no accounts in DB", ErrorType.NO_ACCOUNTS));
    }

    @Override
    public AccountDto addAccount(AccountDto accountDto, Long userId) throws UserNotFoundException {
        Account account = accountMapper.toEntity(accountDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found", ErrorType.USER_NOT_FOUND));
        account.setUser(user);

        return accountMapper.toDto(accountRepository.save(account));
    }

    @Override
    public List<AccountDto> getAccountsByUserId(Long userId) throws NoAccountsException {
        List<AccountDto> accounts = accountMapper.toResponseDtoList(
                accountRepository.findByUserId(userId));
        return accounts.stream()
                .findAny()
                .map(a -> accounts)
                .orElseThrow(() -> new NoAccountsException("There is no accounts in DB", ErrorType.NO_ACCOUNTS));
    }
}
