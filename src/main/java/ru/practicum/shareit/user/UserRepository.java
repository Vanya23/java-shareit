package ru.practicum.shareit.user;

import ru.practicum.shareit.error.exception.IncorrectIdUser;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();

    User getUserById(int userId) throws IncorrectIdUser;

    User addUser(User user);

    void deleteUser(int userId);

    User patchUser(User user) throws IncorrectIdUser;
}
