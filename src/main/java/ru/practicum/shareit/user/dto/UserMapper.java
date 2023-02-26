package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto ans = new UserDto(user.getName(), user.getEmail());
        ans.setId(user.getId());
        return ans;
    }

    public static List<UserDto> toListUserDto(List<User> users) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user :
                users) {
            userDtoList.add(toUserDto(user));
        }
        return userDtoList;
    }
}
