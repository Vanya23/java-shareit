package ru.practicum.shareit.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

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
class BookingRepositoryTest {


    final ItemRepository itemRepository;
    final UserRepository userRepository;

    final BookingRepository bookingRepository;

    long userCount = 0;
    User testUser1;
    User testUser2;
    long itemCount = 0;
    Item testItem1;

    @Test
    void addBooking() {
        testUser1 = makeRepositoryUser(); // владелец
        testUser2 = makeRepositoryUser(); // арендатор
        testItem1 = makeRepositoryItem(testUser1);

        Booking booking = new Booking(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), testItem1, testUser2, BookingStatus.WAITING);

        Booking out = bookingRepository.save(booking);

        assertThat(out.getId(), notNullValue());
        assertThat(out.getItem().getId(), equalTo(testItem1.getId()));
        assertThat(out.getBooker().getId(), equalTo(testUser2.getId()));
    }

    @Test
    void deleteItemRequest() {
        testUser1 = makeRepositoryUser(); // владелец
        testUser2 = makeRepositoryUser(); // арендатор
        testItem1 = makeRepositoryItem(testUser1);

        Booking booking = new Booking(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), testItem1, testUser2, BookingStatus.WAITING);

        Booking out = bookingRepository.save(booking);

        assertThat(out.getId(), notNullValue());
        assertThat(out.getItem().getId(), equalTo(testItem1.getId()));
        assertThat(out.getBooker().getId(), equalTo(testUser2.getId()));

        bookingRepository.delete(out);

        assertThrows(JpaObjectRetrievalFailureException.class, () -> {
            bookingRepository.getReferenceById(out.getId());
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
