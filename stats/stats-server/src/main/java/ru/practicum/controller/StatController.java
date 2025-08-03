package ru.practicum.controller;

import ru.practicum.client.StatsFeignClient;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsCriteriaDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.service.StatService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.util.ConstantsUtil.DATE_TIME_PATTERN;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatController implements StatsFeignClient {
    final StatService statService;
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    static final String STATS_PATH = "/stats";
    static final String HIT_PATH = "/hit";

    @PostMapping(HIT_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@Valid @RequestBody HitDto hitDto) {
        statService.addHit(hitDto);
    }

    @GetMapping(STATS_PATH)
    @ResponseStatus(HttpStatus.OK)
    public List<StatsDto> getStats(@RequestParam("start") String start,
                                   @RequestParam("end") String end,
                                   @RequestParam(value = "uris", required = false) List<String> uris,
                                   @RequestParam(value = "unique", required = false) Boolean unique) {
        StatsCriteriaDto statsCriteriaDto = new StatsCriteriaDto(LocalDateTime.parse(start, formatter),
                LocalDateTime.parse(end, formatter), uris, unique);

        return statService.getStats(statsCriteriaDto);
    }
}
