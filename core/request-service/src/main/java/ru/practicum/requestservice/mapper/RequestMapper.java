package ru.practicum.requestservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.interaction.dto.event.EventFullDto;
import ru.practicum.interaction.dto.request.RequestDto;
import ru.practicum.interaction.dto.user.UserShortDto;
import ru.practicum.requestservice.model.Request;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RequestMapper {

    @Mapping(target = "requester", source = "requesterId")
    @Mapping(target = "event", source = "eventId")
    RequestDto toDto(Request request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventId", source = "eventFullDto.id")  // Исправлено здесь
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "requesterId", source = "requester.id")
    Request toEntity(EventFullDto eventFullDto, UserShortDto requester);
}