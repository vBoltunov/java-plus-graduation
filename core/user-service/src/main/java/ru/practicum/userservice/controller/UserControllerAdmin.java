package ru.practicum.userservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.interaction.dto.user.NewUserRequest;
import ru.practicum.interaction.dto.user.UserDto;
import ru.practicum.interaction.dto.user.UserShortDto;
import ru.practicum.userservice.service.UserService;

import java.util.List;
import java.util.Map;

import static ru.practicum.interaction.util.PathConstants.ADMIN_USERS;
import static ru.practicum.interaction.util.PathConstants.USER_ID;
import static ru.practicum.interaction.util.PathConstants.USER_MAPPED;
import static ru.practicum.interaction.util.PathConstants.USER_SHORT_DTO;

@Slf4j
@RestController
@RequestMapping(ADMIN_USERS)
@RequiredArgsConstructor
@Validated
public class UserControllerAdmin {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Эндпоинт /admin/users. GET запрос на получение админом списка из {} пользователей, начиная с {}",
                size, from);
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest newUser) {
        log.info("Эндпоинт /admin/users. POST запрос на создание админом пользователя {}.", newUser);
        return userService.create(newUser);
    }

    @DeleteMapping(USER_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Positive Long userId) {
        log.info("Эндпоинт /admin/users/{userId}. DELETE запрос на удаление админом пользователя с id {}.", userId);
        userService.delete(userId);
    }

    @GetMapping(USER_MAPPED)
    public Map<Long, UserShortDto> getUsersByIDS(@RequestParam List<Long> ids) {
        return userService.getMapUsers(ids);
    }

    @GetMapping(USER_SHORT_DTO)
    public UserShortDto getUserShortById(@PathVariable Long userId) {
        return userService.getUserShortById(userId);
    }
}