package ru.practicum.eventservice.events.mapper;

import ru.practicum.eventservice.category.mapper.CategoryMapper;
import ru.practicum.interaction.dto.event.enums.UpdateAdminStateAction;
import ru.practicum.interaction.dto.event.enums.EventState;
import ru.practicum.interaction.dto.event.enums.UpdateUserStateAction;
import org.mapstruct.Mapper;

/**
 * Маппер для преобразования действий пользователя/администратора
 * в соответствующие состояния события
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, EventMapper.class})
public interface StateActionMapper {
    default EventState toEventState(UpdateUserStateAction userStateAction) {
        return switch (userStateAction) {
            case SEND_TO_REVIEW -> EventState.PENDING;
            case CANCEL_REVIEW -> EventState.CANCELED;
        };
    }

    default EventState toEventState(UpdateAdminStateAction adminStateAction) {
        return switch (adminStateAction) {
            case PUBLISH_EVENT -> EventState.PUBLISHED;
            case REJECT_EVENT -> EventState.CANCELED;
        };
    }
}