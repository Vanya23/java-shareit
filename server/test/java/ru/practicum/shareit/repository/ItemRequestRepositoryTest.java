package ru.practicum.shareit.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemRequestRepositoryTest {


    final ItemRepository itemRepository;
    final ItemRequestRepository itemRequestRepository;
    final UserRepository userRepository;

    long userCount = 0;
    User testUser1;
    User testUser2;
    long itemCount = 0;
    Item testItem1;

    @Test
    void addItemRequest() {
        testUser1 = makeRepositoryUser(); // владелец
        testUser2 = makeRepositoryUser(); // арендатор
        testItem1 = makeRepositoryItem(testUser1);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("нужна отвертка");
        itemRequest.setRequestor(testUser2);

        ItemRequest out = itemRequestRepository.save(itemRequest);

        assertThat(out.getId(), notNullValue());
        assertThat(out.getDescription(), equalTo(itemRequest.getDescription()));
    }

    @Test
    void deleteItemRequest() {
        testUser1 = makeRepositoryUser(); // владелец
        testUser2 = makeRepositoryUser(); // арендатор
        testItem1 = makeRepositoryItem(testUser1);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("нужна отвертка");
        itemRequest.setRequestor(testUser2);

        ItemRequest out = itemRequestRepository.save(itemRequest);

        assertThat(out.getId(), notNullValue());
        assertThat(out.getDescription(), equalTo(itemRequest.getDescription()));

        itemRequestRepository.delete(out);

        assertThrows(JpaObjectRetrievalFailureException.class, () -> {
            itemRequestRepository.getReferenceById(out.getId());
        });

    }

    private Item makeRepositoryItem(User user) {
        Item item = makeItem("отвертка" + itemCount, "дрель" + itemCount, Boolean.TRUE, user);
        itemCount++;
        Item out = itemRepository.save(item);
        return out;
    }

    private Item makeItem(String name, String description, Boolean available, User user) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setOwner(user);
        return item;
    }


    private User makeUser(String name, String email) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        return user;
    }

    private User makeRepositoryUser() {
        User user = makeUser("Пётр" + userCount, userCount + "some@email.com");
        userCount++;
        User out = userRepository.save(user);
        return out;
    }
}
