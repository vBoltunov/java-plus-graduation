package ru.practicum.eventservice.compilation.service.impl;

import ru.practicum.eventservice.compilation.mapper.CompilationMapper;
import ru.practicum.eventservice.compilation.model.Compilation;
import ru.practicum.eventservice.compilation.repository.CompilationRepository;
import ru.practicum.eventservice.compilation.service.CompilationServiceAdmin;
import ru.practicum.eventservice.events.model.Event;
import ru.practicum.eventservice.events.repository.EventRepository;
import ru.practicum.interaction.dto.event.compilation.CompilationDtoRequest;
import ru.practicum.interaction.dto.event.compilation.CompilationDtoResponse;
import ru.practicum.interaction.dto.event.compilation.CompilationDtoUpdate;
import ru.practicum.interaction.exception.NotFoundException;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationServiceAdminImpl implements CompilationServiceAdmin {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDtoResponse createCompilationAdmin(CompilationDtoRequest compilationDtoRequest) {
        if (compilationDtoRequest.getTitle() == null || compilationDtoRequest.getTitle().isBlank()) {
            throw new ValidationException("Поле title не может быть пустым.");
        }
        List<Event> events = eventRepository.findAllByIdIn(compilationDtoRequest.getEvents());
        CompilationDtoResponse compilationDto = compilationMapper.toCompilationDto(compilationRepository
                .save(compilationMapper.toCompilation(compilationDtoRequest, events)));

        log.info("Создана новая подборка {}", compilationDto);
        return compilationDto;
    }

    @Override
    @Transactional
    public CompilationDtoResponse updateCompilationAdmin(Long compId, CompilationDtoUpdate compilationDtoUpdate) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Подборка с id = %s не найдена", compId)));

        List<Event> events = eventRepository.findAllByIdIn(compilationDtoUpdate.getEvents());

        CompilationDtoResponse compilationDto = compilationMapper.toCompilationDto(compilationRepository
                .save(compilationMapper.toUpdatedCompilation(compilation, compilationDtoUpdate, events)));

        log.info("Обновлена подборка с id {} на {}", compId, compilationDto);
        return compilationDto;
    }

    @Override
    @Transactional
    public void deleteCompilationAdmin(Long compId) {
        compilationRepository.deleteById(compId);
        log.info("Удалена подборка с id {}.", compId);
    }
}