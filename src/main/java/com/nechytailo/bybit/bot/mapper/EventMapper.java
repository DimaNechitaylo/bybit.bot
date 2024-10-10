package com.nechytailo.bybit.bot.mapper;

import com.nechytailo.bybit.bot.bot.dto.TradeEventRequestDto;
import com.nechytailo.bybit.bot.bot.dto.TradeEventResponseDto;
import com.nechytailo.bybit.bot.entity.TradeEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mapping(source = "executeAt", target = "executeAt", qualifiedByName = "stringToLocalDateTime") //TODO maybe delete
    TradeEvent toEntity(TradeEventRequestDto tradeEventRequestDto);

    @Mapping(source = "executeAt", target = "executeAt", qualifiedByName = "localDateTimeToString")
    TradeEventResponseDto toResponseDto(TradeEvent tradeEvent);

    @Mapping(source = "executeAt", target = "executeAt", qualifiedByName = "localDateTimeToString")
    TradeEventRequestDto toRequestDto(TradeEvent tradeEvent);

    List<TradeEventResponseDto> toResponseDtoList(List<TradeEvent> tradeEvents);

    @Named("stringToLocalDateTime")
    default LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, FORMATTER);
    }

    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }
}
