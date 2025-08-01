package ru.practicum.interaction.dto.event.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.interaction.dto.event.requests.EventPublicFilterRequest;

import java.time.LocalDateTime;
import java.util.Objects;

public class StartAndEndValidator implements ConstraintValidator<StartAndEndValid, EventPublicFilterRequest> {
    @Override
    public void initialize(StartAndEndValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(EventPublicFilterRequest filterCriteria, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = filterCriteria.getRangeStart();
        LocalDateTime end = filterCriteria.getRangeEnd();
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return true;
        }
        return start.isBefore(end);
    }
}
