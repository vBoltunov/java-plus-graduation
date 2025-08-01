package ru.practicum.client;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatRestClientImpl implements StatRestClient {
    private final ru.practicum.client.StatsFeignClient statsFeignClient;
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void addHit(HitDto hitDto) {
        log.info("Запрос на добавление статистики");
        try {
            statsFeignClient.addHit(hitDto);
        } catch (Exception e) {
            log.info("Эндпоинт /hit. При запросе на добавление статистики возникла ошибка {}", e.getMessage(), e);
        }
    }

    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Запрос на получение статистики");
        try {
            return statsFeignClient.getStats(start.format(formatter), end.format(formatter), uris, unique);
        } catch (Exception e) {
            log.info("Эндпоинт /stats. При запросе на получение статистики возникла ошибка {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}