package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.error.exception.IncorrectIdUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> map = new HashMap<>();
    private long id = 1;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(map.values());
    }

    @Override
    public User getUserById(long userId) throws IncorrectIdUser {
        if (!map.containsKey(userId)) throw new IncorrectIdUser("IncorrectIdUser");
        return map.get(userId);
    }

    @Override
    public User addUser(User user) {
        user.setId(id++);
        map.put(user.getId(), user);
        return user;
    }

    @Override
    public User patchUser(User user) {
        return null; // не используется временно
    }

    @Override
    public void deleteUser(long userId) {
        map.remove(userId);
    }
}
