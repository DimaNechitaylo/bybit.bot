package com.nechytailo.bybit.bot.mapper;

import com.nechytailo.bybit.bot.bot.dto.TradeEventRequestDto;
import com.nechytailo.bybit.bot.bot.dto.TradeEventResponseDto;
import com.nechytailo.bybit.bot.entity.TradeEvent;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-10T15:39:36+0300",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public TradeEvent toEntity(TradeEventRequestDto tradeEventRequestDto) {
        if ( tradeEventRequestDto == null ) {
            return null;
        }

        TradeEvent tradeEvent = new TradeEvent();

        tradeEvent.setExecuteAt( stringToLocalDateTime( tradeEventRequestDto.getExecuteAt() ) );
        tradeEvent.setDescription( tradeEventRequestDto.getDescription() );
        tradeEvent.setSymbol( tradeEventRequestDto.getSymbol() );
        tradeEvent.setSide( tradeEventRequestDto.getSide() );
        tradeEvent.setQuantity( tradeEventRequestDto.getQuantity() );

        return tradeEvent;
    }

    @Override
    public TradeEventResponseDto toResponseDto(TradeEvent tradeEvent) {
        if ( tradeEvent == null ) {
            return null;
        }

        TradeEventResponseDto.TradeEventResponseDtoBuilder tradeEventResponseDto = TradeEventResponseDto.builder();

        tradeEventResponseDto.executeAt( localDateTimeToString( tradeEvent.getExecuteAt() ) );
        tradeEventResponseDto.description( tradeEvent.getDescription() );
        tradeEventResponseDto.symbol( tradeEvent.getSymbol() );
        tradeEventResponseDto.side( tradeEvent.getSide() );
        tradeEventResponseDto.quantity( tradeEvent.getQuantity() );
        if ( tradeEvent.getStatus() != null ) {
            tradeEventResponseDto.status( tradeEvent.getStatus().name() );
        }

        return tradeEventResponseDto.build();
    }

    @Override
    public TradeEventRequestDto toRequestDto(TradeEvent tradeEvent) {
        if ( tradeEvent == null ) {
            return null;
        }

        TradeEventRequestDto.TradeEventRequestDtoBuilder tradeEventRequestDto = TradeEventRequestDto.builder();

        tradeEventRequestDto.executeAt( localDateTimeToString( tradeEvent.getExecuteAt() ) );
        tradeEventRequestDto.description( tradeEvent.getDescription() );
        tradeEventRequestDto.symbol( tradeEvent.getSymbol() );
        tradeEventRequestDto.side( tradeEvent.getSide() );
        tradeEventRequestDto.quantity( tradeEvent.getQuantity() );

        return tradeEventRequestDto.build();
    }
}
