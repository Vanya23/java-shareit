package ru.practicum.shareit.user;

import ru.practicum.shareit.error.exception.IncorrectIdUser;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();

    User getUserById(long userId) throws IncorrectIdUser;

    User addUser(User user);

    void deleteUser(long userId);

    User patchUser(User user) throws IncorrectIdUser;
}
