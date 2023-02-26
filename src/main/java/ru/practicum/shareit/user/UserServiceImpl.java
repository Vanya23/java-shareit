package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exception.DuplicateEmailException;
import ru.practicum.shareit.error.exception.EmptyEmailException;
import ru.practicum.shareit.error.exception.IncorrectEmailException;
import ru.practicum.shareit.error.exception.IncorrectIdUser;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.toListUserDto(repository.getAllUsers());
    }

    @Override
    public UserDto getUserById(int userId) throws IncorrectIdUser {
        return UserMapper.toUserDto(repository.getUserById(userId));
    }

    @Override
    public UserDto addUser(User user) throws IncorrectEmailException, EmptyEmailException, DuplicateEmailException {
        CheckEmailService.checkAllEmail(user, repository.getAllUsers());
        return UserMapper.toUserDto(repository.addUser(user));
    }

    @Override
    public void deleteUser(int userId) {
        repository.deleteUser(userId);
    }

    @Override
    public UserDto patchUser(int userId, User user) throws IncorrectEmailException, EmptyEmailException, DuplicateEmailException, IncorrectIdUser {
        user.setId(userId);
        if (user.getEmail() != null) CheckEmailService.checkAllEmail(user, repository.getAllUsers());
        return UserMapper.toUserDto(repository.patchUser(user));
    }


}
