package ru.practicum.shareit.user;

import ru.practicum.shareit.error.exception.DuplicateEmailException;
import ru.practicum.shareit.error.exception.EmptyEmailException;
import ru.practicum.shareit.error.exception.IncorrectEmailException;

import java.util.List;

public class CheckEmailService {
    private static final String PATTERN_EMAIL = "^(.+)@(\\S+)$";

    public static boolean checkDuplicateEmail(User user, List<User> users) {
        for (User usr :
                users) {
            if (usr.getEmail().equals(user.getEmail()) && usr.getId() != user.getId()) return false;
        }
        return true;
    }

    public static boolean checkBlankEmail(User user) {
        return !(user.getEmail() == null || user.getEmail().equals(""));

    }

    public static boolean checkCorrectEmail(User user) {
        return user.getEmail().matches(PATTERN_EMAIL);
    }

    public static void checkAllEmail(User user, List<User> users) throws DuplicateEmailException, EmptyEmailException, IncorrectEmailException {
        if (!checkDuplicateEmail(user, users)) throw new DuplicateEmailException("DuplicateEmailException");
        if (!checkBlankEmail(user)) throw new EmptyEmailException("EmptyEmailException");
        if (!checkCorrectEmail(user)) throw new IncorrectEmailException("IncorrectEmailException");
    }
}
