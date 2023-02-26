package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public UserDto fromUserToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public List<UserDto> fromListUserToListUserDto(List<User> users) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user :
                users) {
            userDtoList.add(fromUserToUserDto(user));
        }
        return userDtoList;
    }

    public User fromUserDtoToUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public List<User> fromListUserDtoListToUser(List<UserDto> users) {
        List<User> userList = new ArrayList<>();
        for (UserDto userDto :
                users) {
            userList.add(fromUserDtoToUser(userDto));
        }
        return userList;
    }
}
