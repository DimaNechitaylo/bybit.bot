package com.nechytailo.bybit.bot.mapper;

import com.nechytailo.bybit.bot.bot.dto.TradeEventRequestDto;
import com.nechytailo.bybit.bot.bot.dto.TradeEventResponseDto;
import com.nechytailo.bybit.bot.entity.TradeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-17T21:07:10+0300",
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
        tradeEvent.setName( tradeEventRequestDto.getName() );
        tradeEvent.setFirstSymbol( tradeEventRequestDto.getFirstSymbol() );
        tradeEvent.setSecondSymbol( tradeEventRequestDto.getSecondSymbol() );

        return tradeEvent;
    }

    @Override
    public TradeEventResponseDto toResponseDto(TradeEvent tradeEvent) {
        if ( tradeEvent == null ) {
            return null;
        }

        TradeEventResponseDto.TradeEventResponseDtoBuilder tradeEventResponseDto = TradeEventResponseDto.builder();

        tradeEventResponseDto.executeAt( localDateTimeToString( tradeEvent.getExecuteAt() ) );
        tradeEventResponseDto.name( tradeEvent.getName() );
        tradeEventResponseDto.firstSymbol( tradeEvent.getFirstSymbol() );
        tradeEventResponseDto.secondSymbol( tradeEvent.getSecondSymbol() );
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
        tradeEventRequestDto.name( tradeEvent.getName() );
        tradeEventRequestDto.firstSymbol( tradeEvent.getFirstSymbol() );
        tradeEventRequestDto.secondSymbol( tradeEvent.getSecondSymbol() );

        return tradeEventRequestDto.build();
    }

    @Override
    public List<TradeEventResponseDto> toResponseDtoList(List<TradeEvent> tradeEvents) {
        if ( tradeEvents == null ) {
            return null;
        }

        List<TradeEventResponseDto> list = new ArrayList<TradeEventResponseDto>( tradeEvents.size() );
        for ( TradeEvent tradeEvent : tradeEvents ) {
            list.add( toResponseDto( tradeEvent ) );
        }

        return list;
    }
}
