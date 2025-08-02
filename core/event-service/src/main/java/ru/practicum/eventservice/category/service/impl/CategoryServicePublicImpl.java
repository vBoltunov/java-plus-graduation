package ru.practicum.eventservice.category.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventservice.category.mapper.CategoryMapper;
import ru.practicum.eventservice.category.model.Category;
import ru.practicum.eventservice.category.repository.CategoryRepository;
import ru.practicum.eventservice.category.service.CategoryServicePublic;
import ru.practicum.interaction.dto.event.category.CategoryDto;
import ru.practicum.interaction.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServicePublicImpl implements CategoryServicePublic {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getByIDCategoryPublic(Long catId) {
        CategoryDto categoryDto = categoryMapper.toCategoryDto(findCategoryByIdOrThrow(catId));
        log.info("Получение публичного доступа категории с id {}", catId);
        return categoryDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategoriesPublic(Integer from, Integer size) {
        log.info("Получение публичного списка всех категорий from={}, size={}", from, size);
        return categoryMapper.toCategoryDto(categoryRepository.findAll(PageRequest.of(from / size, size)).toList());
    }

    private Category findCategoryByIdOrThrow(Long catId) {
        Optional<Category> category = categoryRepository.findById(catId);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new NotFoundException("Ресурс не найден");
        }
    }
}
