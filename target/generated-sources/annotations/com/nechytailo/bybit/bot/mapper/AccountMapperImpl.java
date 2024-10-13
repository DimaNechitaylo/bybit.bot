package com.nechytailo.bybit.bot.mapper;

import com.nechytailo.bybit.bot.bot.dto.AccountDto;
import com.nechytailo.bybit.bot.bot.dto.ProxyParamsDto;
import com.nechytailo.bybit.bot.entity.Account;
import com.nechytailo.bybit.bot.entity.ProxyParams;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-14T00:27:50+0300",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public Account toEntity(AccountDto accountDto) {
        if ( accountDto == null ) {
            return null;
        }

        Account account = new Account();

        account.setApiKey( accountDto.getApiKey() );
        account.setApiSecret( accountDto.getApiSecret() );
        account.setProxyParams( proxyParamsDtoToProxyParams( accountDto.getProxyParams() ) );

        return account;
    }

    @Override
    public AccountDto toDto(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountDto.AccountDtoBuilder accountDto = AccountDto.builder();

        accountDto.apiKey( account.getApiKey() );
        accountDto.apiSecret( account.getApiSecret() );
        accountDto.proxyParams( proxyParamsToProxyParamsDto( account.getProxyParams() ) );

        return accountDto.build();
    }

    @Override
    public List<AccountDto> toResponseDtoList(List<Account> byUserId) {
        if ( byUserId == null ) {
            return null;
        }

        List<AccountDto> list = new ArrayList<AccountDto>( byUserId.size() );
        for ( Account account : byUserId ) {
            list.add( toDto( account ) );
        }

        return list;
    }

    @Override
    public List<Account> toEntityList(List<AccountDto> accountDtos) {
        if ( accountDtos == null ) {
            return null;
        }

        List<Account> list = new ArrayList<Account>( accountDtos.size() );
        for ( AccountDto accountDto : accountDtos ) {
            list.add( toEntity( accountDto ) );
        }

        return list;
    }

    protected ProxyParams proxyParamsDtoToProxyParams(ProxyParamsDto proxyParamsDto) {
        if ( proxyParamsDto == null ) {
            return null;
        }

        ProxyParams proxyParams = new ProxyParams();

        proxyParams.setProxyHost( proxyParamsDto.getProxyHost() );
        proxyParams.setProxyPort( proxyParamsDto.getProxyPort() );
        proxyParams.setProxyLogin( proxyParamsDto.getProxyLogin() );
        proxyParams.setProxyPassword( proxyParamsDto.getProxyPassword() );

        return proxyParams;
    }

    protected ProxyParamsDto proxyParamsToProxyParamsDto(ProxyParams proxyParams) {
        if ( proxyParams == null ) {
            return null;
        }

        ProxyParamsDto.ProxyParamsDtoBuilder proxyParamsDto = ProxyParamsDto.builder();

        proxyParamsDto.proxyHost( proxyParams.getProxyHost() );
        proxyParamsDto.proxyPort( proxyParams.getProxyPort() );
        proxyParamsDto.proxyLogin( proxyParams.getProxyLogin() );
        proxyParamsDto.proxyPassword( proxyParams.getProxyPassword() );

        return proxyParamsDto.build();
    }
}
