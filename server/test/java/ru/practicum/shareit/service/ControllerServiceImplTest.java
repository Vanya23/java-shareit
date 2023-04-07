package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

//@Rollback(false)
@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ControllerServiceImplTest {
    // реализация тестов

    private final UserController userController;
    private final ItemController itemController;
    private final ItemRequestController itemRequestController;
    private final BookingController bookingController;
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    private long userCount = 0;
    private long testUserId1;
    private long testUserId2;
    private long itemCount = 0;
    private long testItemId1;

    @Test
    void userControllerTest() {
        List<UserDto> out = userController.getAllUsers();

        assertThat(out, notNullValue());

    }

    @Test
    void itemControllerTest() {
        List<ItemDtoOut> out = itemController.searchItemByText("");

        assertThat(out, notNullValue());

    }

    @Test
    void itemRequestControllerTest() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        ItemRequestDtoIn itemRequestDtoIn = new ItemRequestDtoIn();
        itemRequestDtoIn.setDescription("нужна отвертка");

        ItemRequestDtoOut out = itemRequestController.addItemRequest(testUserId2, itemRequestDtoIn);

        assertThat(out.getId(), notNullValue());
        assertThat(out.getDescription(), equalTo(itemRequestDtoIn.getDescription()));

    }

    @Test
    void bookingControllerTest() {

        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        BookingDtoInput bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), testItemId1);

        BookingDtoOut out = bookingController.addBooking(testUserId2, bookingDto);

        assertThat(out.getId(), notNullValue());
        assertThat(out.getItem().getId(), equalTo(testItemId1));
        assertThat(out.getBooker().getId(), equalTo(testUserId2));

    }


    private long getTestItemId() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка" + itemCount, "дрель" + itemCount, Boolean.TRUE);
        itemCount++;
        ItemDtoOut out = itemService.addItem(testUserId1, itemDto);
        return out.getId();
    }


    private ItemDtoIn makeItemDtoIn(String name, String description, Boolean available) {
        ItemDtoIn itemDto = new ItemDtoIn();
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);
        return itemDto;
    }


    private UserDto makeUserDto(String name, String email) {
        UserDto dto = new UserDto();
        dto.setEmail(email);
        dto.setName(name);
        return dto;
    }

    private long getTestUserId() {
        UserDto userDto = makeUserDto("Пётр" + userCount, userCount + "some@email.com");
        userCount++;
        UserDto out = userService.addUser(userDto);
        return out.getId();
    }


}
