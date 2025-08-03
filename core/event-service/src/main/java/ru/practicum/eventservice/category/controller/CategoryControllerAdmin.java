package ru.practicum.eventservice.category.controller;

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
import ru.practicum.eventservice.category.service.CategoryServiceAdmin;
import ru.practicum.interaction.dto.event.category.CategoryDto;
import ru.practicum.interaction.dto.event.category.CategoryDtoNew;

import static ru.practicum.interaction.util.ConstantsUtil.ADMIN_CATEGORIES;
import static ru.practicum.interaction.util.ConstantsUtil.CATEGORY_ID;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(ADMIN_CATEGORIES)
public class CategoryControllerAdmin {
    private final CategoryServiceAdmin categoryServiceAdmin;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid CategoryDtoNew newCategoryDto) {
        log.info("POST /admin/categories - Запрос на создание админом новой категории {}.",
                newCategoryDto);
        return new ResponseEntity<>(categoryServiceAdmin.createCategoryAdmin(newCategoryDto), HttpStatus.CREATED);
    }

    @PatchMapping(CATEGORY_ID)
    public ResponseEntity<CategoryDto> update(@RequestBody @Valid CategoryDtoNew newCategoryDto,
                                              @PathVariable Long catId) {
        log.info("PATCH /admin/categories/{} - Запрос по на обновление админом категории с id {}.", catId,
                catId);
        return new ResponseEntity<>(categoryServiceAdmin.updateCategoryAdmin(newCategoryDto, catId), HttpStatus.OK);
    }

    @DeleteMapping(CATEGORY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {
        log.info("DELETE /admin/categories/{} - Запрос на удаление админом категории с id {}.", catId, catId);
        categoryServiceAdmin.deleteCategoryAdmin(catId);
    }
}