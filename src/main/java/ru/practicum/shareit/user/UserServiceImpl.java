package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
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
    private final UserRepository repository;
    private final CheckEmailService checkEmailService;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.fromListUserToListUserDto(repository.getAllUsers());
    }

    @Override
    public UserDto getUserById(long userId) throws IncorrectIdUser {
        return userMapper.fromUserToUserDto(repository.getUserById(userId));
    }

    @Override
    public UserDto addUser(UserDto userDto) throws DuplicateEmailException {
        // На соответствие почты и null проверка объекта не выполняется т.к. сделана в @Validated
        if (!checkEmailService.checkDuplicateEmail(userDto, repository.getAllUsers()))
            throw new DuplicateEmailException("DuplicateEmailException");
        return userMapper.fromUserToUserDto(repository.addUser(userMapper.fromUserDtoToUser(userDto)));
    }

    @Override
    public void deleteUser(long userId) {
        repository.deleteUser(userId);
    }

    @Override
    public UserDto patchUser(long userId, UserDto userDto) throws IncorrectEmailException, EmptyEmailException, DuplicateEmailException, IncorrectIdUser {
        userDto.setId(userId);
        if (userDto.getEmail() != null) checkEmailService.checkAllEmail(userDto, repository.getAllUsers());
        User temp = repository.getUserById(userDto.getId());
        if (userDto.getName() != null) {
            temp.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            temp.setEmail(userDto.getEmail());
        }
        return userMapper.fromUserToUserDto(temp);
    }


}
