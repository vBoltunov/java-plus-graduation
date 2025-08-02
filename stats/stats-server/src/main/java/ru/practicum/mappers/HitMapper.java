package ru.practicum.mappers;

import ru.practicum.dto.HitDto;
import ru.practicum.model.Hit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.util.ConstantsUtil.DATE_TIME_PATTERN;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HitMapper {
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public Hit mapToEndpointHit(HitDto dto) {
        Hit hit = new Hit();
        hit.setApp(dto.getApp());
        hit.setIp(dto.getIp());
        hit.setUri(dto.getUri());
        LocalDateTime timestamp = LocalDateTime.parse(dto.getTimestamp(), dateTimeFormatter);
        hit.setTimestamp(timestamp);

        return hit;
    }
}
