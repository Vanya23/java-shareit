package ru.practicum.shareit.user;

import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.InternalServerErrortException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(long userId) throws NotFoundException;

    UserDto addUser(UserDto userDto);

    void deleteUser(long userId);

    UserDto patchUser(long userId, UserDto userDto) throws InternalServerErrortException, BadRequestException;
}
