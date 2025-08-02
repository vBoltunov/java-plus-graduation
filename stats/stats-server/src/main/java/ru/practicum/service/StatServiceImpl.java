package ru.practicum.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsCriteriaDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.mappers.HitMapper;
import ru.practicum.model.Hit;
import ru.practicum.repository.HitRepository;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatServiceImpl implements StatService {
    final HitRepository hitRepository;
    final HitMapper hitMapper;

    @Override
    @Transactional
    public void addHit(HitDto hitDto) {
        Hit hit = hitMapper.mapToEndpointHit(hitDto);
        hitRepository.save(hit);
        log.info("Статистика {} сохранена в базу данных", hit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatsDto> getStats(StatsCriteriaDto params) {
        if (params.getStart().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Время начала не может быть в будущем");
        }

        boolean isUnique = params.getUnique() != null && params.getUnique();
        List<StatsDto> statsToReturn;
        boolean paramsIsNotExists = params.getUris() == null || params.getUris().isEmpty();

        if (!isUnique) {
            if (paramsIsNotExists) {
                statsToReturn = hitRepository.getAllStats(params.getStart(), params.getEnd());
            } else {
                statsToReturn = hitRepository.getStats(params.getUris(), params.getStart(), params.getEnd());
            }
        } else {
            if (paramsIsNotExists) {
                statsToReturn = hitRepository.getAllStatsUniqueIp(params.getStart(), params.getEnd());
            } else {
                statsToReturn = hitRepository.getStatsUniqueIp(params.getUris(), params.getStart(), params.getEnd());
            }
        }

        log.info("Статистика {} получена из базы данных", statsToReturn);
        return statsToReturn;
    }
}
