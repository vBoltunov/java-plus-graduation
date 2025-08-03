package ru.practicum.eventservice.category.service;

import ru.practicum.interaction.dto.event.category.CategoryDto;
import ru.practicum.interaction.dto.event.category.CategoryDtoNew;

public interface CategoryServiceAdmin {
    CategoryDto createCategoryAdmin(CategoryDtoNew newCategoryDto);

    CategoryDto updateCategoryAdmin(CategoryDtoNew newCategoryDto, Long catId);

    void deleteCategoryAdmin(Long catId);
}