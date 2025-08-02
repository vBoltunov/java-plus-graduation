package ru.practicum.userservice.service;

import ru.practicum.interaction.dto.user.NewUserRequest;
import ru.practicum.interaction.dto.user.UserDto;
import ru.practicum.interaction.dto.user.UserShortDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserDto create(NewUserRequest newUser);

    void delete(Long id);

    Map<Long, UserShortDto> getMapUsers(List<Long> ids);

    UserShortDto getUserShortById(Long userId);
}