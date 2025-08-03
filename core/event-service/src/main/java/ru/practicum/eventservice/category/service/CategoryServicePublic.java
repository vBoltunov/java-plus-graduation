package ru.practicum.eventservice.category.service;

import ru.practicum.interaction.dto.event.category.CategoryDto;

import java.util.List;

public interface CategoryServicePublic {
    CategoryDto getByIDCategoryPublic(Long catId);

    List<CategoryDto> getAllCategoriesPublic(Integer from, Integer size);
}
