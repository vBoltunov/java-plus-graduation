package ru.practicum.subscriptionservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.interaction.dto.subscription.SubscriptionDto;
import ru.practicum.subscriptionservice.model.Subscription;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscriber.id", source = "subscription.subscriberId")
    @Mapping(target = "subscribedTo.id", source = "subscription.subscribedToId")
    @Mapping(target = "created", ignore = true)
    SubscriptionDto toSubscriptionShortDto(Subscription subscription);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscription.subscriberId", source = "subscriberId")
    @Mapping(target = "subscription.subscribedToId", source = "subscribedToId")
    @Mapping(target = "created", ignore = true)
    Subscription toSubscription(@MappingTarget Subscription subscription,
                                Long subscriberId, Long subscribedToId);
}