package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.error.ErrorResponse;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
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
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemServiceImplTest {
    // реализация тестов

    private final ItemService service;
    private final UserService userService;

    private final ItemRepository repository;
    private final BookingService bookingService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private long userCount = 0;
    private long testUserId1;
    private long testUserId2;
    private long itemCount = 0;
    private long testItemId1;

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
    void patchItemNotFoundException() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка", "дрель", Boolean.TRUE);
        testUserId1 = getTestUserId();

        ItemDtoOut out = service.addItem(testUserId1, itemDto);

        ItemDtoIn itemDtoPatch = makeItemDtoIn("отвертка++", "дрель++", Boolean.FALSE);


        assertThrows(NotFoundException.class, () -> {
            service.patchItem(testUserId1 * 2, out.getId(), itemDtoPatch);
        });
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
    void getItemByIdNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            service.getItemById(0, 0);
        });

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

        List<ItemDtoOut> listOut = service.getAllItemByUserIdPage(testUserId1, "0", "100");

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

        List<ItemDtoOut> listOut = service.searchItemByTextPage("отв", "0", "100");
        assertThat(listOut.get(0).getId(), notNullValue());
        assertThat(listOut.get(0).getName(), equalTo(itemDto.getName()));
        assertThat(listOut.get(0).getDescription(), equalTo(itemDto.getDescription()));
    }

    @Test
    void postComment() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка", "дрель", Boolean.TRUE);
        testUserId1 = getTestUserId();   // владелец
        ItemDtoOut out = service.addItem(testUserId1, itemDto);
        testUserId2 = getTestUserId(); // арендатор
        long testUserId3 = getTestUserId(); // арендатор
        testItemId1 = out.getId();

        BookingDtoInput bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusNanos(1),
                LocalDateTime.now().plusNanos(2), testItemId1);

        BookingDtoOut outB = bookingService.addBooking(testUserId2, bookingDto);
        outB = bookingService.patchBooking(outB.getId(), testUserId1, Boolean.TRUE);

        CommentDtoIn commentDtoIn = new CommentDtoIn("comment");
        CommentDtoOut outCom = service.postComment(testUserId2, out.getId(), commentDtoIn);
        assertThat(outCom.getId(), notNullValue());


        bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), testItemId1);
        outB = bookingService.addBooking(testUserId3, bookingDto);

        outB = bookingService.patchBooking(outB.getId(), testUserId1, Boolean.TRUE);

        ItemDtoOut ans = service.getItemById(testItemId1, testUserId1);
        assertThat(ans, notNullValue());


    }

    @Test
    void postCommentBadRequestException2() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка", "дрель", Boolean.TRUE);
        testUserId1 = getTestUserId();
        ItemDtoOut out = service.addItem(testUserId1, itemDto);
        CommentDtoIn commentDtoIn = new CommentDtoIn("comment");

        assertThrows(BadRequestException.class, () -> {
            service.postComment(testUserId1, out.getId(), commentDtoIn);
        });

    }

    @Test
    void postCommentBadRequestException() {
        CommentDtoIn commentDtoIn = new CommentDtoIn("comment");
        assertThrows(BadRequestException.class, () -> {
            service.postComment(0, 0, commentDtoIn);
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
                1L, "Petr", "hello", Boolean.TRUE, 1L);

        assertThat(itemDtoOutForItemRequest.getId(), notNullValue());
        assertThat(itemDtoOutForItemRequest.getDescription(), notNullValue());
        assertThat(itemDtoOutForItemRequest.getName(), equalTo("Petr"));
        assertThat(itemDtoOutForItemRequest.getAvailable(), equalTo(Boolean.TRUE));
        assertThat(itemDtoOutForItemRequest.getRequestId(), notNullValue());

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
        assertThat(comment2, equalTo(comment));
    }

    @Test
    void itemMapperTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("");
        item.setDescription("");
        item.setAvailable(Boolean.TRUE);
        item.setOwner(new User(1L, "", ""));
        item.setRequest(new ItemRequest(1L, null, null, null));
        ItemDtoOutForItemRequest in = itemMapper.fromItemToItemDtoOutForItemRequest(item);
        List<Item> items = List.of(item);
        List<ItemDtoOutForItemRequest> inList = itemMapper.fromListItemToListDtoOutForItemRequest(items);
        Item item1 = new Item();
        item1.setId(0L);
        assertThat(in, notNullValue());
        assertThat(inList, notNullValue());
        assertThat(item.equals(item1), equalTo(Boolean.FALSE));

    }

    @Test
    void itemTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("");
        item.setDescription("");
        item.setAvailable(Boolean.TRUE);
        item.setOwner(new User(1L, "", ""));
        item.setRequest(new ItemRequest(1L, null, null, null));

        Item item1 = new Item(item.getName(), item.getDescription(), item.getAvailable());

        assertThat(item, equalTo(item));
        assertThat(item1.hashCode(), notNullValue());

    }

    @Test
    void commentDtoInTest() {

        CommentDtoIn commentDtoIn = new CommentDtoIn("comment");
        commentDtoIn.setText(commentDtoIn.getText() + 1);

        assertThat(commentDtoIn.getText(), equalTo("comment1"));

    }

    @Test
    void commentDtoOutTest() {

        CommentDtoOut commentDtoOut = new CommentDtoOut();
        commentDtoOut.setId(1L);
        commentDtoOut.setText("comment1");
        commentDtoOut.setAuthorName("Name");
        commentDtoOut.setCreated("Name");


        assertThat(commentDtoOut.getText(), equalTo("comment1"));

    }

    @Test
    void commentMapperTest() {
        CommentDtoIn commentDtoIn = new CommentDtoIn("comment");
        Comment comment = commentMapper.fromCommentDtoInToComment(commentDtoIn);
        comment.setId(1L);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(new Item());
        User user = new User(1L, "Petr", "mail");
        comment.setAuthor(user);
        assertThat(comment, notNullValue());
        assertThat(comment.equals(comment), equalTo(Boolean.TRUE));
        assertThat(comment.hashCode(), equalTo(comment.hashCode()));

        CommentDtoOut out = commentMapper.fromCommentToCommentDtoOut(comment);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        List<CommentDtoOut> outList = commentMapper.fromListCommentToCommentDtoOut(comments);

        assertThat(out, notNullValue());
        assertThat(outList, notNullValue());


    }

    @Test
    void itemDtoOutTest() {
        ItemDtoOut itemDtoOut = new ItemDtoOut();
        itemDtoOut.setId(1L);
        itemDtoOut.setName("a");
        itemDtoOut.setDescription("abc");
        itemDtoOut.setAvailable(Boolean.TRUE);


        assertThat(itemDtoOut.getId(), equalTo(1L));

    }

    @Test
    void itemDtoOutForItemRequestTest() {
        ItemDtoOutForItemRequest itemDtoOutForItemRequest = new ItemDtoOutForItemRequest();
        itemDtoOutForItemRequest.setId(1L);
        itemDtoOutForItemRequest.setName("a");
        itemDtoOutForItemRequest.setDescription("abc");
        itemDtoOutForItemRequest.setAvailable(Boolean.TRUE);


        assertThat(itemDtoOutForItemRequest.getId(), equalTo(1L));

    }

    private long getTestItemId() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка" + itemCount, "дрель" + itemCount, Boolean.TRUE);
        itemCount++;
        ItemDtoOut out = service.addItem(testUserId1, itemDto);
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
