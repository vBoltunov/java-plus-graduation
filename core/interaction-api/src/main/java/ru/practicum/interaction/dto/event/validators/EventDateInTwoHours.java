package ru.practicum.interaction.dto.event.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EventDateInTwoHoursValidator.class)
public @interface EventDateInTwoHours {
    String message() default "Start time must be before end time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
