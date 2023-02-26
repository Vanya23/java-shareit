package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.exception.DuplicateEmailException;
import ru.practicum.shareit.error.exception.EmptyEmailException;
import ru.practicum.shareit.error.exception.IncorrectEmailException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Component
public class CheckEmailService {

    public boolean checkDuplicateEmail(UserDto user, List<User> users) {
        for (User usr :
                users) {
            if (usr.getEmail().equals(user.getEmail()) && !usr.getId().equals(user.getId())) return false;
        }
        return true;
    }

    public boolean checkBlankEmail(UserDto user) {
        return !(user.getEmail() == null || user.getEmail().equals(""));

    }

    public boolean checkCorrectEmail(UserDto user) {
        String patternEmail = "^(.+)@(\\S+)$";
        return user.getEmail().matches(patternEmail);
    }

    public void checkAllEmail(UserDto user, List<User> users) throws DuplicateEmailException, EmptyEmailException, IncorrectEmailException {
        if (!checkDuplicateEmail(user, users)) throw new DuplicateEmailException("DuplicateEmailException");
        if (!checkBlankEmail(user)) throw new EmptyEmailException("EmptyEmailException");
        if (!checkCorrectEmail(user)) throw new IncorrectEmailException("IncorrectEmailException");
    }
}
