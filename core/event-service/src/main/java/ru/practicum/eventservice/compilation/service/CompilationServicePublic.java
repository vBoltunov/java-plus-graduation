package ru.practicum.eventservice.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.interaction.dto.event.compilation.CompilationDtoResponse;

import java.util.List;

public interface CompilationServicePublic {
    CompilationDtoResponse getCompilationByIdPublic(Long compId);

    List<CompilationDtoResponse> getAllCompilationsPublic(Boolean pinned, Pageable pageRequest);
}