package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {

        return userClient.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getAllUsers(@PathVariable long userId) {

        return userClient.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Validated({Create.class}) @RequestBody UserDto userDto) {

        return userClient.addUser(userDto);
    }


    @PatchMapping("/{userId}")
    public ResponseEntity<Object> patchUser(@PathVariable long userId, @Validated({Update.class}) @RequestBody UserDto userDto) {
        return userClient.patchUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {

        return userClient.deleteUser(userId);

    }


}
