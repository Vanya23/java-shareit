package ru.practicum.shareit.repository;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserRepositoryTest {

    final UserRepository userRepository;


    @Test
    void addUserAndGetUserById() {
        User user = makeUser("Пётр", "some@email.com");
        User out = userRepository.save(user);

        User userOut = userRepository.getReferenceById(out.getId());
        assertThat(userOut.getId(), notNullValue());
        assertThat(userOut.getName(), equalTo(user.getName()));
        assertThat(userOut.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void deleteUser() {
        User user = makeUser("Пётр", "some@email.com");
        User out = userRepository.save(user);

        User userOut = userRepository.getReferenceById(out.getId());
        assertThat(userOut.getId(), notNullValue());
        assertThat(userOut.getName(), equalTo(user.getName()));
        assertThat(userOut.getEmail(), equalTo(user.getEmail()));

        userRepository.deleteById(out.getId());

        assertThrows(JpaObjectRetrievalFailureException.class, () -> {
            userRepository.getReferenceById(out.getId());
        });


    }


    private User makeUser(String name, String email) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        return user;
    }

}