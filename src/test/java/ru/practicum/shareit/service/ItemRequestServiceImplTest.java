package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
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
class ItemRequestServiceImplTest {

    private final ItemRequestService service;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemRepository repository;

    private long userCount = 0;
    private long testUserId1;
    private long testUserId2;
    private long itemCount = 0;
    private long testItemId1;


    @Test
    void addItemRequest() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        ItemRequestDtoIn itemRequestDtoIn = new ItemRequestDtoIn();
        itemRequestDtoIn.setDescription("нужна отвертка");

        ItemRequestDtoOut out = service.addItemRequest(testUserId2, itemRequestDtoIn);

        assertThat(out.getId(), notNullValue());
        assertThat(out.getDescription(), equalTo(itemRequestDtoIn.getDescription()));
    }

    @Test
    void getAllByUserId() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        ItemRequestDtoIn itemRequestDtoIn = new ItemRequestDtoIn();
        itemRequestDtoIn.setDescription("нужна отвертка");
        ItemRequestDtoOut out = service.addItemRequest(testUserId2, itemRequestDtoIn);
        ItemDtoIn itemDto = makeItemDtoIn("отвертка" + itemCount, "дрель" + itemCount, Boolean.TRUE);
        itemDto.setRequestId(out.getId());
        itemCount++;
        ItemDtoOut out2 = itemService.addItem(testUserId1, itemDto);
        testItemId1 = out2.getId();


        List<ItemRequestDtoOut> outList = service.getAllByUserId(testUserId2);

        assertThat(outList.get(0).getId(), notNullValue());
        assertThat(outList.get(0).getDescription(), equalTo(itemRequestDtoIn.getDescription()));
    }

    @Test
    void getAllOtherUsers() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        ItemRequestDtoIn itemRequestDtoIn = new ItemRequestDtoIn();
        itemRequestDtoIn.setDescription("нужна отвертка");

        ItemRequestDtoOut out = service.addItemRequest(testUserId2, itemRequestDtoIn);
        List<ItemRequestDtoOut> outList = service.getAllOtherUsers(testUserId1);

        assertThat(outList.get(0).getId(), notNullValue());
        assertThat(outList.get(0).getDescription(), equalTo(itemRequestDtoIn.getDescription()));
    }

    @Test
    void getAllOtherUsersPage() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        ItemRequestDtoIn itemRequestDtoIn = new ItemRequestDtoIn();
        itemRequestDtoIn.setDescription("нужна отвертка");

        ItemRequestDtoOut out = service.addItemRequest(testUserId2, itemRequestDtoIn);
        Page<ItemRequestDtoOut> outPage = service.getAllOtherUsersPage(testUserId1, "0", "100");
        List<ItemRequestDtoOut> outList = outPage.getContent();

        assertThat(outList.get(0).getId(), notNullValue());
        assertThat(outList.get(0).getDescription(), equalTo(itemRequestDtoIn.getDescription()));
    }

    @Test
    void getByRequestId() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        ItemRequestDtoIn itemRequestDtoIn = new ItemRequestDtoIn();
        itemRequestDtoIn.setDescription("нужна отвертка");

        ItemRequestDtoOut out = service.addItemRequest(testUserId2, itemRequestDtoIn);
        ItemRequestDtoOut outGetByRequestId = service.getByRequestId(testUserId2, out.getId());

        assertThat(outGetByRequestId.getId(), notNullValue());
        assertThat(outGetByRequestId.getDescription(), equalTo(itemRequestDtoIn.getDescription()));
    }

    @Test
    void itemRequestTest() {
        ItemRequest itemRequest1 = new ItemRequest(
                1L, "desc", new User(), LocalDateTime.now());
        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setId(itemRequest1.getId());
        itemRequest2.setDescription(itemRequest1.getDescription());
        itemRequest2.setRequestor(itemRequest1.getRequestor());
        itemRequest2.setCreated(itemRequest1.getCreated());

        assertThat(itemRequest1, equalTo(itemRequest2));
        assertThat(itemRequest1.hashCode(), equalTo(itemRequest2.hashCode()));

    }

    @Test
    void itemRequestDtoOut() {
        ItemRequestDtoOut itemRequest1 = new ItemRequestDtoOut(
                1L, "desc", "new User()", null);
        ItemRequestDtoOut itemRequest2 = new ItemRequestDtoOut();
        itemRequest2.setId(itemRequest1.getId());
        itemRequest2.setDescription(itemRequest1.getDescription());
        itemRequest2.setItems(itemRequest1.getItems());
        itemRequest2.setCreated(itemRequest1.getCreated());

        assertThat(itemRequest1.hashCode(), notNullValue());

    }

    @Test
    void getByRequestIdNotFoundException() {


        assertThrows(NotFoundException.class, () -> {
            service.getByRequestId(-1, 1);
        });


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
