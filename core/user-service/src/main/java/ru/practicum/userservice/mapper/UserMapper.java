package ru.practicum.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.practicum.interaction.dto.user.NewUserRequest;
import ru.practicum.interaction.dto.user.UserDto;
import ru.practicum.interaction.dto.user.UserShortDto;
import ru.practicum.userservice.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    User toUser(NewUserRequest newUserRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    UserShortDto toUserShortDto(User user);
}
