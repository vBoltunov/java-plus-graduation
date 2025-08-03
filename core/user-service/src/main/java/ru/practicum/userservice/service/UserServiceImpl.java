package ru.practicum.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.interaction.dto.user.NewUserRequest;
import ru.practicum.interaction.dto.user.UserDto;
import ru.practicum.interaction.dto.user.UserShortDto;
import ru.practicum.interaction.exception.ConflictException;
import ru.practicum.interaction.exception.NotFoundException;
import ru.practicum.interaction.exception.ValidationException;
import ru.practicum.userservice.mapper.UserMapper;
import ru.practicum.userservice.model.User;
import ru.practicum.userservice.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Параметры пагинации должны быть неотрицательными и size > 0");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<User> users = (ids == null || ids.isEmpty()) ?
                userRepository.findAll(pageable).getContent() :
                userRepository.findByIdIn(ids, pageable).getContent();
        return users.stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto create(NewUserRequest newUser) {
        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new ConflictException(String.format("Пользователь с email = %s уже существует", newUser.getEmail()));
        }
        User user = userMapper.toUser(newUser);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        User user = checkUserExists(id);
        userRepository.deleteById(id);
        log.info("Пользователь {} удалён", user);
    }

    @Override
    public Map<Long, UserShortDto> getMapUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            log.info("Невозможно получить пользователей. Список пользователей пуст");
            return Collections.emptyMap();
        }

        return userRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(
                        User::getId,
                        userMapper::toUserShortDto
                ));
    }

    @Override
    public UserShortDto getUserShortById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Id пользователя не может быть null");
        }

        return userRepository.findById(userId)
                .map(userMapper::toUserShortDto)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %s не найден", userId)));
    }

    private User checkUserExists(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %s не найден", id)));
    }
}