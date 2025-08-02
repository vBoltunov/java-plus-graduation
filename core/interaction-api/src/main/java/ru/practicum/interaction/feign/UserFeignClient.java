package ru.practicum.interaction.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.interaction.dto.user.NewUserRequest;
import ru.practicum.interaction.dto.user.UserDto;
import ru.practicum.interaction.dto.user.UserShortDto;

import java.util.List;
import java.util.Map;

import static ru.practicum.interaction.util.ConstantsUtil.ADMIN_USERS;
import static ru.practicum.interaction.util.ConstantsUtil.USER_ID;
import static ru.practicum.interaction.util.ConstantsUtil.USER_MAPPED;
import static ru.practicum.interaction.util.ConstantsUtil.USER_SHORT_DTO;

@FeignClient(name = "user-service", path = ADMIN_USERS)
public interface UserFeignClient {
    @PostMapping
    UserDto createUser(@RequestBody NewUserRequest newUserRequest);

    @GetMapping
    List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                            @RequestParam(defaultValue = "0") int from,
                            @RequestParam(defaultValue = "10") int size);

    @DeleteMapping(USER_ID)
    void deleteUser(@PathVariable Long userId);

    @GetMapping(USER_MAPPED)
    Map<Long, UserShortDto> getUsersByIDS(@RequestParam List<Long> ids);

    @GetMapping(USER_SHORT_DTO)
    UserShortDto getUserShortById(@PathVariable Long userId);
}