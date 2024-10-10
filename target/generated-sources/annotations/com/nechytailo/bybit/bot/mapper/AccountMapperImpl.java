package com.nechytailo.bybit.bot.mapper;

import com.nechytailo.bybit.bot.bot.dto.AccountDto;
import com.nechytailo.bybit.bot.bot.dto.ProxyParamsDto;
import com.nechytailo.bybit.bot.entity.Account;
import com.nechytailo.bybit.bot.entity.ProxyParams;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-10T15:39:36+0300",
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
