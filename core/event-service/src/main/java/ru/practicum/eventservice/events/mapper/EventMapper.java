package ru.practicum.eventservice.events.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.eventservice.category.mapper.CategoryMapper;
import ru.practicum.eventservice.category.model.Category;
import ru.practicum.interaction.dto.event.EventFullDto;
import ru.practicum.interaction.dto.event.EventShortDto;
import ru.practicum.interaction.dto.event.NewEventDto;
import ru.practicum.interaction.dto.event.requests.UpdateEventAdminRequest;
import ru.practicum.interaction.dto.event.requests.UpdateEventUserRequest;
import ru.practicum.interaction.dto.user.UserShortDto;
import ru.practicum.eventservice.events.model.Event;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, StateActionMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "initiatorId", source = "initiator.id")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    Event toEvent(NewEventDto newEventDto, UserShortDto initiator, Category category);

    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "rating", ignore = true)
    EventFullDto toEventFullDto(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "initiatorId", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "state", source = "updateEventUserRequest.stateAction")
    @Mapping(target = "publishedOn", ignore = true)
    Event toUpdatedEvent(@MappingTarget Event event, UpdateEventUserRequest updateEventUserRequest, Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "initiatorId", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "state", source = "updateEventAdminRequest.stateAction")
    @Mapping(target = "publishedOn", ignore = true)
    Event toUpdatedEvent(@MappingTarget Event event, UpdateEventAdminRequest updateEventAdminRequest, Category category);

    @Mapping(target = "id", source = "event.id")
    @Mapping(target = "initiator", source = "user")
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "rating", ignore = true)
    EventShortDto toEventShortDto(Event event, UserShortDto user);
}