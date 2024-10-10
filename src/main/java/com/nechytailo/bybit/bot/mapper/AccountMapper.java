package com.nechytailo.bybit.bot.mapper;

import com.nechytailo.bybit.bot.bot.dto.AccountDto;
import com.nechytailo.bybit.bot.entity.Account;
import com.nechytailo.bybit.bot.entity.TradeEvent;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toEntity(AccountDto accountDto);
    AccountDto toDto(Account account);
    List<AccountDto> toResponseDtoList(List<Account> byUserId);
}
