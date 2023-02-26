package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.error.exception.IncorrectIdUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Integer, User> map = new HashMap<>();
    private int id = 1;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(map.values());
    }

    @Override
    public User getUserById(int userId) throws IncorrectIdUser {
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
    public User patchUser(User user) throws IncorrectIdUser {
        User temp = getUserById(user.getId());
        if (user.getName() != null) {
            temp.setName(user.getName());
        }
        if (user.getEmail() != null) {
            temp.setEmail(user.getEmail());
        }
        return temp;
    }

    @Override
    public void deleteUser(int userId) {
        map.remove(userId);
    }
}
