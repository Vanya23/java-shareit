package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.ErrorResponse;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoOutForItemRequest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
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
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemServiceImplTest {
    // реализация тестов

    private final ItemService service;
    private final UserService userService;
    private final ItemRepository repository;

    private long testUserId1;


    @Test
    void addItem() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка", "дрель", Boolean.TRUE);
        testUserId1 = getTestUserId();
        ItemDtoOut out = service.addItem(testUserId1, itemDto);
        assertThat(out.getId(), notNullValue());
        assertThat(out.getName(), equalTo(itemDto.getName()));
        assertThat(out.getDescription(), equalTo(itemDto.getDescription()));
    }

    @Test
    void patchItem() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка", "дрель", Boolean.TRUE);
        testUserId1 = getTestUserId();

        ItemDtoOut out = service.addItem(testUserId1, itemDto);

        ItemDtoIn itemDtoPatch = makeItemDtoIn("отвертка++", "дрель++", Boolean.FALSE);

        ItemDtoOut outPatch = service.patchItem(testUserId1, out.getId(), itemDtoPatch);

        assertThat(out.getId(), notNullValue());
        assertThat(outPatch.getName(), equalTo(itemDtoPatch.getName()));
        assertThat(outPatch.getDescription(), equalTo(itemDtoPatch.getDescription()));
    }

    @Test
    void getItemById() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка", "дрель", Boolean.TRUE);
        testUserId1 = getTestUserId();
        ItemDtoOut out = service.addItem(testUserId1, itemDto);

        ItemDtoOut out2 = service.getItemById(out.getId(), testUserId1);
        assertThat(out2.getId(), notNullValue());
        assertThat(out2.getName(), equalTo(itemDto.getName()));
        assertThat(out2.getDescription(), equalTo(itemDto.getDescription()));
    }

    @Test
    void getAllItemByUserId() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка", "дрель", Boolean.TRUE);
        testUserId1 = getTestUserId();
        ItemDtoOut out = service.addItem(testUserId1, itemDto);

        List<ItemDtoOut> listOut = service.getAllItemByUserId(testUserId1);
        assertThat(listOut.get(0).getId(), notNullValue());
        assertThat(listOut.get(0).getName(), equalTo(itemDto.getName()));
        assertThat(listOut.get(0).getDescription(), equalTo(itemDto.getDescription()));
    }

    @Test
    void getAllItemByUserIdPage() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка", "дрель", Boolean.TRUE);
        testUserId1 = getTestUserId();
        ItemDtoOut out = service.addItem(testUserId1, itemDto);

        Page<ItemDtoOut> pageOut = service.getAllItemByUserIdPage(testUserId1, "0", "100");
        List<ItemDtoOut> listOut = pageOut.getContent();

        assertThat(listOut.get(0).getId(), notNullValue());
        assertThat(listOut.get(0).getName(), equalTo(itemDto.getName()));
        assertThat(listOut.get(0).getDescription(), equalTo(itemDto.getDescription()));
    }

    @Test
    void searchItemByText() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка", "дрель", Boolean.TRUE);
        testUserId1 = getTestUserId();
        ItemDtoOut out = service.addItem(testUserId1, itemDto);

        List<ItemDtoOut> listOut = service.searchItemByText("отв");
        assertThat(listOut.get(0).getId(), notNullValue());
        assertThat(listOut.get(0).getName(), equalTo(itemDto.getName()));
        assertThat(listOut.get(0).getDescription(), equalTo(itemDto.getDescription()));
    }

    @Test
    void searchItemByTextPage() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка", "дрель", Boolean.TRUE);
        testUserId1 = getTestUserId();
        ItemDtoOut out = service.addItem(testUserId1, itemDto);

        Page<ItemDtoOut> pageOut = service.searchItemByTextPage("отв", "0", "100");
        List<ItemDtoOut> listOut = pageOut.getContent();
        assertThat(listOut.get(0).getId(), notNullValue());
        assertThat(listOut.get(0).getName(), equalTo(itemDto.getName()));
        assertThat(listOut.get(0).getDescription(), equalTo(itemDto.getDescription()));
    }

    @Test
    void postComment() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка", "дрель", Boolean.TRUE);
        testUserId1 = getTestUserId();
        ItemDtoOut out = service.addItem(testUserId1, itemDto);
        CommentDtoIn commentDtoIn = new CommentDtoIn("comment");

        assertThrows(BadRequestException.class, () -> {
            service.postComment(testUserId1, out.getId(), commentDtoIn);
        });

    }


    @Test
    void test404() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка", "дрель", Boolean.TRUE);
        testUserId1 = getTestUserId();

        assertThrows(NotFoundException.class, () -> {
            service.addItem(testUserId1 * 100, itemDto);
        });

    }

    @Test
    void itemDtoOutForItemRequest() {
        ItemDtoOutForItemRequest itemDtoOutForItemRequest = new ItemDtoOutForItemRequest(
                1L, "Petr", null, Boolean.TRUE, 1L);

        assertThat(itemDtoOutForItemRequest.getId(), notNullValue());
        assertThat(itemDtoOutForItemRequest.getName(), equalTo("Petr"));
        assertThat(itemDtoOutForItemRequest.getAvailable(), equalTo(Boolean.TRUE));
    }

    @Test
    void errorResponse() {
        ErrorResponse errorResponse = new ErrorResponse("error");
        ErrorResponse errorResponse2 = new ErrorResponse();
        errorResponse2.setError(errorResponse.getError());
        assertThat(errorResponse.getError(), equalTo("error"));
        assertThat(errorResponse2.getError(), equalTo("error"));
    }

    @Test
    void commentTest() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("comment");
        comment.setItem(new Item());
        comment.setAuthor(new User());
        comment.setCreated(LocalDateTime.now());
        Comment comment2 = new Comment(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor(),
                comment.getCreated()
        );


        assertThat(comment2.getId(), equalTo(1L));
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
        UserDto userDto = makeUserDto("Пётр", "some@email.com");
        UserDto out = userService.addUser(userDto);
        testUserId1 = out.getId();
        return testUserId1;
    }
}
