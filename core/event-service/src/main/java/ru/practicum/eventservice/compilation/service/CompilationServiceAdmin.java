package ru.practicum.eventservice.compilation.service;

import ru.practicum.interaction.dto.event.compilation.CompilationDtoRequest;
import ru.practicum.interaction.dto.event.compilation.CompilationDtoResponse;
import ru.practicum.interaction.dto.event.compilation.CompilationDtoUpdate;

public interface CompilationServiceAdmin {
    CompilationDtoResponse createCompilationAdmin(CompilationDtoRequest compilationDtoRequest);

    CompilationDtoResponse updateCompilationAdmin(Long compId, CompilationDtoUpdate compilationDtoRequest);

    void deleteCompilationAdmin(Long compId);
}