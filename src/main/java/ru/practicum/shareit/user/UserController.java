package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.error.exception.DuplicateEmailException;
import ru.practicum.shareit.error.exception.EmptyEmailException;
import ru.practicum.shareit.error.exception.IncorrectEmailException;
import ru.practicum.shareit.error.exception.IncorrectIdUser;
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
    public UserDto getAllUsers(@PathVariable int userId) throws IncorrectIdUser {

        return service.getUserById(userId);
    }

    @PostMapping
    public UserDto addUser(@RequestBody User user) throws DuplicateEmailException, IncorrectEmailException, EmptyEmailException {
        return service.addUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto patchUser(@PathVariable int userId, @RequestBody User user) throws IncorrectEmailException, EmptyEmailException, DuplicateEmailException, IncorrectIdUser {
        return service.patchUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        service.deleteUser(userId);

    }


}
