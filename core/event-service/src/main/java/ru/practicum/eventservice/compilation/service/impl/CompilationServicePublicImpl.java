package ru.practicum.eventservice.compilation.service.impl;

import com.querydsl.core.BooleanBuilder;
import ru.practicum.eventservice.compilation.mapper.CompilationMapper;
import ru.practicum.eventservice.compilation.model.Compilation;
import ru.practicum.eventservice.compilation.model.QCompilation;
import ru.practicum.eventservice.compilation.repository.CompilationRepository;
import ru.practicum.eventservice.compilation.service.CompilationServicePublic;
import ru.practicum.eventservice.events.mapper.EventMapper;
import ru.practicum.interaction.dto.event.EventShortDto;
import ru.practicum.interaction.dto.event.compilation.CompilationDtoResponse;
import ru.practicum.interaction.dto.user.UserShortDto;
import ru.practicum.interaction.exception.NotFoundException;
import ru.practicum.interaction.feign.UserFeignClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationServicePublicImpl implements CompilationServicePublic {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final UserFeignClient userFeignClient;
    private final EventMapper eventMapper;

    @Override
    @Transactional(readOnly = true)
    public CompilationDtoResponse getCompilationByIdPublic(Long compId) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(compId);

        if (compilationOptional.isEmpty()) {
            throw new NotFoundException(String.format("Подборка с id = %s не найдена", compId));
        }
        CompilationDtoResponse compilationDto = compilationMapper.toCompilationDto(compilationOptional.get());

        log.info("Получили подборку с id {}.", compId);
        return compilationDto;
    }

    @Override
    public List<CompilationDtoResponse> getAllCompilationsPublic(Boolean pinned, Pageable pageRequest) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (pinned != null) {
            booleanBuilder.and(QCompilation.compilation.pinned.eq(pinned));
        }

        List<Compilation> compilations = compilationRepository
                .findAll(booleanBuilder, pageRequest)
                .toList();

        List<CompilationDtoResponse> compilationDtoListToReturn = new ArrayList<>();

        for (Compilation compilation : compilations) {
            CompilationDtoResponse compilationDto = compilationMapper.toCompilationDto(compilation);
            List<EventShortDto> eventShortDtoList = compilation.getEvents().stream()
                    .map(event -> {
                        UserShortDto initiatorShortDto = userFeignClient
                                .getUserShortById(event.getInitiatorId());

                        return eventMapper.toEventShortDto(event, initiatorShortDto);
                    })
                    .toList();

            compilationDto.setEvents(new HashSet<>(eventShortDtoList));
            compilationDtoListToReturn.add(compilationDto);
        }

        log.info("Получили подборки по заданным параметрам");
        return compilationDtoListToReturn;
    }
}