package ru.practicum.eventservice.category.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.eventservice.category.service.CategoryServicePublic;
import ru.practicum.interaction.dto.event.category.CategoryDto;

import java.util.List;

import static ru.practicum.interaction.util.ConstantsUtil.CATEGORIES;
import static ru.practicum.interaction.util.ConstantsUtil.CATEGORY_ID;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(CATEGORIES)
public class CategoryControllerPublic {
    private final CategoryServicePublic categoryServicePublic;

    @GetMapping(CATEGORY_ID)
    public ResponseEntity<CategoryDto> getById(@PathVariable Long catId) {
        log.info("GET /categories - Запрос на получение категории(public) с id {}.", catId);
        return new ResponseEntity<>(categoryServicePublic.getByIDCategoryPublic(catId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll(
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("GET /categories - Запрос на получение всех категорий(public) с параметрами from={}, size={}.",
                from, size);
        return new ResponseEntity<>(categoryServicePublic.getAllCategoriesPublic(from, size), HttpStatus.OK);
    }
}