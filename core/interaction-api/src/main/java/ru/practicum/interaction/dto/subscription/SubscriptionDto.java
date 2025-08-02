package ru.practicum.interaction.dto.subscription;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.interaction.dto.user.UserShortDto;

/**
 * DTO representing subscription data exchanged via API.
 *
 * Fields:
 * - `id` – Unique identifier of the subscription.
 * - `subscriber` – Simplified DTO of the subscribing user.
 * - `subscribedTo` – Simplified DTO of the user being followed.
 * - `created` – String representation of the subscription creation timestamp.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionDto {
    Long id;
    UserShortDto subscriber;
    UserShortDto subscribedTo;
    String created;
}
