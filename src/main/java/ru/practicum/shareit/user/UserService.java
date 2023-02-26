package ru.practicum.shareit.user;

import ru.practicum.shareit.error.exception.DuplicateEmailException;
import ru.practicum.shareit.error.exception.EmptyEmailException;
import ru.practicum.shareit.error.exception.IncorrectEmailException;
import ru.practicum.shareit.error.exception.IncorrectIdUser;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(int userId) throws IncorrectIdUser;

    UserDto addUser(User user) throws DuplicateEmailException, IncorrectEmailException, EmptyEmailException;

    void deleteUser(int userId);

    UserDto patchUser(int userId, User user) throws IncorrectEmailException, EmptyEmailException, DuplicateEmailException, IncorrectIdUser;
}
