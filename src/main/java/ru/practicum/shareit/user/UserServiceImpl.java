package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.InternalServerErrortException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final CheckEmailService checkEmailService;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.fromListUserToListUserDto(repository.findAll());
    }

    @Override
    public UserDto getUserById(long userId) throws NotFoundException {
        try {
            return userMapper.fromUserToUserDto(repository.getReferenceById(userId));
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("NotFoundException in UserService.getUserById()");
        }
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        // На соответствие почты и null проверка объекта не выполняется т.к. сделана в @Validated
        // по условию задачи проверка уникальности почты выполняется в БД
//        if (!checkEmailService.checkDuplicateEmail(userDto, repository.findAll()))
//            throw new DuplicateEmailException("DuplicateEmailException");
        return userMapper.fromUserToUserDto(repository.save(userMapper.fromUserDtoToUser(userDto)));
    }

    @Override
    public void deleteUser(long userId) {
        repository.deleteById(userId);
    }

    @Override
    public UserDto patchUser(long userId, UserDto userDto) throws InternalServerErrortException, BadRequestException {
        userDto.setId(userId);
        if (userDto.getEmail() != null) checkEmailService.checkAllEmail(userDto, repository.findAll());
        User temp = repository.getReferenceById(userDto.getId());
        if (userDto.getName() != null) {
            temp.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            temp.setEmail(userDto.getEmail());
        }
        repository.saveAndFlush(temp); // обновление базы
        return userMapper.fromUserToUserDto(repository.getReferenceById(userDto.getId()));
    }


}
