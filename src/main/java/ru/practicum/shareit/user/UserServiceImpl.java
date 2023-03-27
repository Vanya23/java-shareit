package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

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
    public UserDto getUserById(long userId) {
        return userMapper.fromUserToUserDto(repository.getReferenceById(userId));
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
    public UserDto patchUser(long userId, UserDto userDto) {
        userDto.setId(userId);
        User userForPatch = repository.getReferenceById(userDto.getId());
        if (Strings.isNotBlank(userDto.getName())) {
            userForPatch.setName(userDto.getName());
        }
        if (Strings.isNotBlank(userDto.getEmail())) {
            userForPatch.setEmail(userDto.getEmail());
        }
        return userMapper.fromUserToUserDto(userForPatch);
    }


}
