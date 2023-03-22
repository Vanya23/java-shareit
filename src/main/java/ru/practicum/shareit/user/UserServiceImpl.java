package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.fromListUserToListUserDto(repository.findAll());
    }

    @Override
    public UserDto getUserById(long userId)   {
        try {
            return userMapper.fromUserToUserDto(repository.getReferenceById(userId));
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("NotFoundException in UserService.getUserById()");
        }
    }

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        // На соответствие почты и null проверка объекта не выполняется т.к. сделана в @Validated
        // по условию задачи проверка уникальности почты выполняется в БД
        return userMapper.fromUserToUserDto(repository.save(userMapper.fromUserDtoToUser(userDto)));
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        repository.deleteById(userId);
    }

    @Override
    @Transactional
    public UserDto patchUser(long userId, UserDto userDto)  {
        userDto.setId(userId);
        User temp = repository.getReferenceById(userDto.getId());
        if (Strings.isNotBlank(userDto.getName())) {
            temp.setName(userDto.getName());
        }
        if (Strings.isNotBlank(userDto.getEmail())) {
            temp.setEmail(userDto.getEmail());
        }
        return userMapper.fromUserToUserDto(repository.getReferenceById(userDto.getId()));
    }


}
