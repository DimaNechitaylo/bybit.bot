package com.nechytailo.bybit.bot.mapper;

import com.nechytailo.bybit.bot.bot.dto.AccountBalanceDto;
import com.nechytailo.bybit.bot.dto.BalanceInfoResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountBalanceMapper {
    AccountBalanceDto toAccountBalanceDto(BalanceInfoResponseDto accountBalanceDto);
    BalanceInfoResponseDto toResponseDto(AccountBalanceDto accountBalanceDto);
    List<BalanceInfoResponseDto> toResponseDtoList(List<AccountBalanceDto> accountBalanceDtos);
    List<AccountBalanceDto> toAccountBalanceDtoList(List<BalanceInfoResponseDto> balanceInfoResponseDtos);

}
