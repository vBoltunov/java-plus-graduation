package ru.practicum.eventservice.compilation.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.eventservice.compilation.service.CompilationServicePublic;
import ru.practicum.interaction.dto.event.compilation.CompilationDtoResponse;

import java.util.List;

import static ru.practicum.interaction.util.ConstantsUtil.COMPILATIONS;
import static ru.practicum.interaction.util.ConstantsUtil.COMPILATION_ID;

@RequestMapping(COMPILATIONS)
@RestController
@Slf4j
@RequiredArgsConstructor
public class CompilationControllerPublic {
    private final CompilationServicePublic compilationServicePublic;

    @GetMapping(COMPILATION_ID)
    public ResponseEntity<CompilationDtoResponse> getById(@PositiveOrZero @PathVariable Long compId) {
        log.info("GET /compilations/{} - Публичный запрос на получение подборки с id {}.", compId, compId);
        return new ResponseEntity<>(compilationServicePublic.getCompilationByIdPublic(compId),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CompilationDtoResponse>> getAll(@RequestParam(required = false) Boolean pinned,
                                                               @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                               @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /compilations - Запрос на получение(public) подборок с параметрами " +
                "pinned = {}, from = {}, size = {}.", pinned, from, size);
        return new ResponseEntity<>(compilationServicePublic.getAllCompilationsPublic(pinned, PageRequest.of(from, size)
        ), HttpStatus.OK);
    }
}
