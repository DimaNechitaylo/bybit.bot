package com.nechytailo.bybit.bot.mapper;

import com.nechytailo.bybit.bot.bot.dto.TradeEventRequestDto;
import com.nechytailo.bybit.bot.bot.dto.TradeEventResponseDto;
import com.nechytailo.bybit.bot.entity.TradeEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Методы маппинга для DTO в Entity
    @Mapping(source = "executeAt", target = "executeAt", qualifiedByName = "stringToLocalDateTime")
    TradeEvent toEntity(TradeEventRequestDto tradeEventRequestDto);

    // Методы маппинга для Entity в ResponseDTO
    @Mapping(source = "executeAt", target = "executeAt", qualifiedByName = "localDateTimeToString")
    TradeEventResponseDto toResponseDto(TradeEvent tradeEvent);

    // Методы маппинга для Entity в RequestDTO
    @Mapping(source = "executeAt", target = "executeAt", qualifiedByName = "localDateTimeToString")
    TradeEventRequestDto toRequestDto(TradeEvent tradeEvent);

    // Кастомные методы преобразования
    @Named("stringToLocalDateTime")
    default LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, FORMATTER);
    }

    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }
}
