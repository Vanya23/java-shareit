package ru.practicum.shareit.user;

import ru.practicum.shareit.error.exception.DuplicateEmailException;
import ru.practicum.shareit.error.exception.EmptyEmailException;
import ru.practicum.shareit.error.exception.IncorrectEmailException;
import ru.practicum.shareit.error.exception.IncorrectIdUser;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(long userId) throws IncorrectIdUser;

    UserDto addUser(UserDto userDto) throws DuplicateEmailException, IncorrectEmailException, EmptyEmailException;

    void deleteUser(long userId);

    UserDto patchUser(long userId, UserDto userDto) throws IncorrectEmailException, EmptyEmailException, DuplicateEmailException, IncorrectIdUser;
}
