package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@Rollback(false)
@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@DataJpaTest
class UserServiceImplTest {
    // реализация тестов

    private final UserService service;
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final EntityManager em;

    private long id;


    @BeforeEach
    void setUp() {
    }


    @Test
    void addUser() {
        UserDto userDto = makeUserDto("Пётр", "some@email.com");
        UserDto out = service.addUser(userDto);


        User user = repository.getReferenceById(out.getId());
        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void patchUser() {
        UserDto userDto = makeUserDto("Пётр", "some@email.com");
        UserDto userDto2 = makeUserDto("Пётр2", "some@email.com");
        UserDto out = service.addUser(userDto);
        out = service.patchUser(out.getId(), userDto2);


        User user = repository.getReferenceById(out.getId());
        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto2.getName()));
        assertThat(user.getEmail(), equalTo(userDto2.getEmail()));
    }

    @Test
    void getAllUsers() {
        List<UserDto> userDtoList = makeListUserDto("Пётр", "some@email.com", 3);
        List<UserDto> outList = saveUserList(userDtoList);
        List<User> users = new ArrayList<>();
        outList = service.getAllUsers();
        for (UserDto userDto :
                outList) {
            User user = repository.getReferenceById(userDto.getId());
            users.add(user);
        }
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            UserDto userDto = userDtoList.get(i);
            assertThat(user.getId(), notNullValue());
            assertThat(user.getName(), equalTo(userDto.getName()));
            assertThat(user.getEmail(), equalTo(userDto.getEmail()));
        }
    }


    @Test
    void getUserById() {
        UserDto userDto = makeUserDto("Пётр", "some@email.com");
        UserDto out = service.addUser(userDto);

        UserDto user = service.getUserById(out.getId());
        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void deleteUser() {
        UserDto userDto = makeUserDto("Пётр", "some@email.com");
        UserDto out = service.addUser(userDto);
        service.deleteUser(out.getId());
        assertThrows(JpaObjectRetrievalFailureException.class, () -> {
            service.getUserById(out.getId());
        });

    }

    @Test
    void addUser500() {
        UserDto userDto = makeUserDto("Пётр", "some@email.com");
        service.addUser(userDto);
        assertThrows(DataIntegrityViolationException.class, () -> {
            service.addUser(userDto);
        });


    }

    @Test
    void testUser() {
        User user1 = new User(1L, "Petr", "abc@mail.com");
        User user2 = new User();
        user2.setId(user1.getId());
        user2.setName(user1.getName());
        user2.setEmail(user1.getEmail());

        assertThat(user1, equalTo(user2));
        assertThat(user1.hashCode(), equalTo(user2.hashCode()));

    }

    @Test
    void fromListUserToListUserDto() {
        User user1 = new User(1L, "Petr", "abc@mail.com");
        User user2 = new User();
        user2.setId(user1.getId());
        user2.setName(user1.getName());
        user2.setEmail(user1.getEmail());
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        List<UserDto> usersDtos = userMapper.fromListUserToListUserDto(users);

        assertThat(usersDtos.size(), equalTo(2));

    }


    private List<UserDto> makeListUserDto(String name, String email, int number) {
        List<UserDto> userDtoList = new ArrayList<>();
        String myPrefix = "Person";
        for (int i = 0; i < number; i++) {
            UserDto userDto = makeUserDto(name + myPrefix + i, i + myPrefix + email);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    private List<UserDto> saveUserList(List<UserDto> userDtoList) {
        List<UserDto> ans = new ArrayList<>();
        for (UserDto userDto :
                userDtoList) {
            ans.add(service.addUser(userDto));
        }
        return ans;
    }

    private UserDto makeUserDto(String name, String email) {
        UserDto dto = new UserDto();
        dto.setEmail(email);
        dto.setName(name);
        return dto;
    }
}
