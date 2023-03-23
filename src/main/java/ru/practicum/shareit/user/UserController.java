package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService service;

    @GetMapping
    public List<UserDto> getAllUsers() {

        return service.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getAllUsers(@PathVariable long userId) {

        return service.getUserById(userId);
    }

    @PostMapping
    public UserDto addUser(@Validated({Create.class}) @RequestBody UserDto userDto) {

        return service.addUser(userDto);
    }


    @PatchMapping("/{userId}")
    public UserDto patchUser(@PathVariable long userId, @Validated({Update.class}) @RequestBody UserDto userDto) {
        return service.patchUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        service.deleteUser(userId);

    }


}
