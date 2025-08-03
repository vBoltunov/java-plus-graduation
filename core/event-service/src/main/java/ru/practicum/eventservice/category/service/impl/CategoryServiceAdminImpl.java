package ru.practicum.eventservice.category.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventservice.category.mapper.CategoryMapper;
import ru.practicum.eventservice.category.model.Category;
import ru.practicum.eventservice.category.repository.CategoryRepository;
import ru.practicum.eventservice.category.service.CategoryServiceAdmin;
import ru.practicum.interaction.dto.event.category.CategoryDto;
import ru.practicum.interaction.dto.event.category.CategoryDtoNew;
import ru.practicum.interaction.exception.ConflictException;
import ru.practicum.interaction.exception.NotFoundException;
import ru.practicum.eventservice.events.repository.EventRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto createCategoryAdmin(CategoryDtoNew categoryDtoNew) {
        if (categoryRepository.existsByName(categoryDtoNew.getName())) {
            throw new ConflictException(String.format("Категория с именем %s уже существует.", categoryDtoNew.getName()));
        }
        log.info("Создание новой категории админом {}.", categoryDtoNew);
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(categoryDtoNew)));
    }

    @Override
    @Transactional
    public CategoryDto updateCategoryAdmin(CategoryDtoNew categoryDtoNew, Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Ресурс не найден");
        }
        Optional<Category> existingCategory = categoryRepository.findByName(categoryDtoNew.getName());
        if (existingCategory.isPresent() && !existingCategory.get().getId().equals(catId)) {
            throw new ConflictException(String.format("Категория с именем '%s' уже существует.", categoryDtoNew.getName()));
        }
        Category category = categoryMapper.toCategory(categoryDtoNew);
        category.setId(catId);
        log.info("Обновление админом категории с id {} на {}.", catId, categoryDtoNew);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategoryAdmin(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Не найдено событий для категории с id = %s", catId)));
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("Невозможно удалить. В категории содержатся события.");
        }
        categoryRepository.deleteById(catId);
        log.info("Админ удалил категорию с id {}", catId);

    }
}