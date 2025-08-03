package ru.practicum.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsCriteriaDto;
import ru.practicum.dto.StatsDto;

import java.util.List;

public interface StatService {
    void addHit(HitDto hitDto);

    List<StatsDto> getStats(StatsCriteriaDto params);
}
