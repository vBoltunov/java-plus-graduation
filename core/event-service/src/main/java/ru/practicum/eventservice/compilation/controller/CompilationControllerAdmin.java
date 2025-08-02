package ru.practicum.eventservice.compilation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.eventservice.compilation.service.CompilationServiceAdmin;
import ru.practicum.interaction.dto.event.compilation.CompilationDtoRequest;
import ru.practicum.interaction.dto.event.compilation.CompilationDtoResponse;
import ru.practicum.interaction.dto.event.compilation.CompilationDtoUpdate;

import static ru.practicum.interaction.util.ConstantsUtil.ADMIN_COMPILATIONS;
import static ru.practicum.interaction.util.ConstantsUtil.COMPILATION_ID;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(ADMIN_COMPILATIONS)
public class CompilationControllerAdmin {
    private final CompilationServiceAdmin compilationServiceAdmin;

    @PostMapping
    public ResponseEntity<CompilationDtoResponse> create(@Valid @RequestBody CompilationDtoRequest compilationDtoRequest) {
        log.info("POST /admin/compilations - Запрос на создание админом новой подборки {}.",
                compilationDtoRequest);
        return new ResponseEntity<>(compilationServiceAdmin.createCompilationAdmin(compilationDtoRequest),
                HttpStatus.CREATED);
    }

    @PatchMapping(COMPILATION_ID)
    @ResponseStatus(HttpStatus.OK)
    public CompilationDtoResponse update(@PathVariable("compId") Long compId,
                                 @Valid @RequestBody CompilationDtoUpdate compilationDtoUpdate) {
        log.info("PATCH /admin/compilations/{} - Запрос на обновление админом подборки на {}.",
                compId, compilationDtoUpdate);
        return compilationServiceAdmin.updateCompilationAdmin(compId, compilationDtoUpdate);
    }

    @DeleteMapping(COMPILATION_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("compId") Long compId) {
        compilationServiceAdmin.deleteCompilationAdmin(compId);
        log.info("DELETE /admin/compilations/{} - Запрос  на удаление админом подборки с id {}.", compId,
                compId);
    }
}