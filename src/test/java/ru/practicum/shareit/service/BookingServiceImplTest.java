package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingDtoOutputForItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

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
class BookingServiceImplTest {

    private final BookingService service;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemRepository repository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;

    private long userCount = 0;
    private long testUserId1;
    private long testUserId2;
    private long itemCount = 0;
    private long testItemId1;


    @Test
    void addBooking() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        BookingDtoInput bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), testItemId1);

        BookingDtoOut out = service.addBooking(testUserId2, bookingDto);

        assertThat(out.getId(), notNullValue());
        assertThat(out.getItem().getId(), equalTo(testItemId1));
        assertThat(out.getBooker().getId(), equalTo(testUserId2));
    }

    @Test
    void patchBooking() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        BookingDtoInput bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), testItemId1);

        BookingDtoOut out = service.addBooking(testUserId2, bookingDto);
        BookingDtoOut outPatch = service.patchBooking(out.getId(), testUserId1, Boolean.TRUE);

        assertThat(out.getId(), notNullValue());
        assertThat(outPatch.getItem().getId(), equalTo(testItemId1));
        assertThat(outPatch.getBooker().getId(), equalTo(testUserId2));
        assertThat(outPatch.getStatus(), equalTo(BookingStatus.APPROVED.toString()));
    }

    @Test
    void getBookingById() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        BookingDtoInput bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), testItemId1);

        BookingDtoOut out = service.addBooking(testUserId2, bookingDto);
        BookingDtoOut outgetBookingById = service.getBookingById(out.getId(), testUserId2);

        assertThat(outgetBookingById.getId(), notNullValue());
        assertThat(outgetBookingById.getItem().getId(), equalTo(testItemId1));
        assertThat(outgetBookingById.getBooker().getId(), equalTo(testUserId2));
    }

    @Test
    void getAllBookingsByOwner() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        BookingDtoInput bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), testItemId1);

        BookingDtoOut out = service.addBooking(testUserId2, bookingDto);
        List<BookingDtoOut> outgetBookingById = service.getAllBookingsByOwner(testUserId1, "ALL");

        assertThat(outgetBookingById.get(0).getId(), notNullValue());
        assertThat(outgetBookingById.get(0).getItem().getId(), equalTo(testItemId1));
        assertThat(outgetBookingById.get(0).getBooker().getId(), equalTo(testUserId2));
    }

    @Test
    void getAllBookingsByOwnerPage() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        BookingDtoInput bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), testItemId1);

        BookingDtoOut out = service.addBooking(testUserId2, bookingDto);
        Page<BookingDtoOut> outgetBookingByIdPage = service.getAllBookingsByOwnerPage(testUserId1, "ALL", "0", "100");
        List<BookingDtoOut> outgetBookingById = outgetBookingByIdPage.getContent();

        assertThat(outgetBookingById.get(0).getId(), notNullValue());
        assertThat(outgetBookingById.get(0).getItem().getId(), equalTo(testItemId1));
        assertThat(outgetBookingById.get(0).getBooker().getId(), equalTo(testUserId2));
    }

    @Test
    void getAllBookingsByUserId() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        BookingDtoInput bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), testItemId1);

        BookingDtoOut out = service.addBooking(testUserId2, bookingDto);
        List<BookingDtoOut> outgetAllBookingsByUserId =
                service.getAllBookingsByUserId(testUserId2, "ALL");

        assertThat(outgetAllBookingsByUserId.get(0).getId(), notNullValue());
        assertThat(outgetAllBookingsByUserId.get(0).getItem().getId(), equalTo(testItemId1));
        assertThat(outgetAllBookingsByUserId.get(0).getBooker().getId(), equalTo(testUserId2));
    }

    @Test
    void getAllBookingsByUserIdPage() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        BookingDtoInput bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), testItemId1);

        BookingDtoOut out = service.addBooking(testUserId2, bookingDto);
        Page<BookingDtoOut> outgetAllBookingsByUserIdPage = service.getAllBookingsByUserIdPage(testUserId2, "ALL", "0", "100");
        List<BookingDtoOut> outgetAllBookingsByUserId =
                outgetAllBookingsByUserIdPage.getContent();

        assertThat(outgetAllBookingsByUserId.get(0).getId(), notNullValue());
        assertThat(outgetAllBookingsByUserId.get(0).getItem().getId(), equalTo(testItemId1));
        assertThat(outgetAllBookingsByUserId.get(0).getBooker().getId(), equalTo(testUserId2));
    }

    @Test
    void bookingDtoOutputForItem() {
        BookingDtoOutputForItem bookingDtoOutputForItem = new BookingDtoOutputForItem(
                1L, null, null, 1L, 1L, null);

        assertThat(bookingDtoOutputForItem.getId(), notNullValue());
        assertThat(bookingDtoOutputForItem.getItemId(), equalTo(1L));
        assertThat(bookingDtoOutputForItem.getBookerId(), equalTo(1L));
    }


    @Test
    void getAllBookingsByOwnerNotFound() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        new BookingDtoInput(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), testItemId1);

        assertThrows(NotFoundException.class, () -> {
            service.getAllBookingsByOwner(testUserId1 * 100, "ALL");
        });


    }

    @Test
    void itemMapperTest() {
        testUserId1 = getTestUserId(); // владелец
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = getTestItemId();

        BookingDtoInput bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), testItemId1);
        BookingDtoOut out = service.addBooking(testUserId2, bookingDto);
        Booking booking = bookingRepository.getReferenceById(out.getId());
        Booking booking2 = new Booking(
                booking.getStart(), booking.getEnd(), booking.getItem(),
                booking.getBooker(), booking.getStatus()
        );
        booking2.setId(booking.getId());
        BookingDtoOutputForItem in = itemMapper.fromBookingToBookingDtoOutputForItem(
                booking);

        assertThat(in, notNullValue());
        assertThat(booking.hashCode(), notNullValue());
        assertThat(booking, equalTo(booking2));


    }

    @Test
    void bookingDtoOutTest() {
        BookingDtoOut bookingDtoOut = new BookingDtoOut();
        bookingDtoOut.setId(1L);
        bookingDtoOut.setStart("");
        bookingDtoOut.setEnd("");
        bookingDtoOut.setItem(null);
        bookingDtoOut.setBooker(null);
        bookingDtoOut.setStatus("");
        BookingDtoOut bookingDtoOut2 = new BookingDtoOut(
                bookingDtoOut.getId(), bookingDtoOut.getStart(),
                bookingDtoOut.getEnd(), bookingDtoOut.getItem(),
                bookingDtoOut.getBooker(), bookingDtoOut.getStatus()
        );

        assertThat(bookingDtoOut, notNullValue());
        assertThat(bookingDtoOut2, notNullValue());

    }

    @Test
    void bookingDtoOutputForItemTest() {
        BookingDtoOutputForItem bookingDtoOut = new BookingDtoOutputForItem();
        bookingDtoOut.setId(1L);
        bookingDtoOut.setStart("");
        bookingDtoOut.setEnd("");
        bookingDtoOut.setItemId(1L);
        bookingDtoOut.setBookerId(1L);
        bookingDtoOut.setStatus("");
        BookingDtoOutputForItem bookingDtoOut2 = new BookingDtoOutputForItem(
                bookingDtoOut.getId(), bookingDtoOut.getStart(),
                bookingDtoOut.getEnd(), bookingDtoOut.getItemId(),
                bookingDtoOut.getBookerId(), bookingDtoOut.getStatus()
        );

        assertThat(bookingDtoOut, notNullValue());
        assertThat(bookingDtoOut2, notNullValue());

    }

    @Test
    void bigTest() {
        ItemDtoIn itemDto = makeItemDtoIn("отвертка", "дрель", Boolean.TRUE);
        testUserId1 = getTestUserId();   // владелец
        ItemDtoOut out = itemService.addItem(testUserId1, itemDto);
        testUserId2 = getTestUserId(); // арендатор
        testItemId1 = out.getId();

        // прошлое approved
        BookingDtoInput bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusNanos(1),
                LocalDateTime.now().plusNanos(2), testItemId1);
        BookingDtoOut outB = service.addBooking(testUserId2, bookingDto);
        outB = service.patchBooking(outB.getId(), testUserId1, Boolean.TRUE);

        // будущее approved
        bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), testItemId1);
        outB = service.addBooking(testUserId2, bookingDto);
        outB = service.patchBooking(outB.getId(), testUserId1, Boolean.TRUE);
        // текущее approved
        bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusNanos(1),
                LocalDateTime.now().plusHours(1), testItemId1);
        outB = service.addBooking(testUserId2, bookingDto);
        outB = service.patchBooking(outB.getId(), testUserId1, Boolean.TRUE);
        // будущее waiting
        bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(11), testItemId1);
        // будущее REJECTED
        bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(20),
                LocalDateTime.now().plusDays(21), testItemId1);
        outB = service.addBooking(testUserId2, bookingDto);
        outB = service.patchBooking(outB.getId(), testUserId1, Boolean.FALSE);

        assertThrows(RuntimeException.class, () -> {
            service.getAllBookingsByUserId(testUserId1, "ALL123");
        });

        String[] states = new String[]{"ALL", "WAITING", "REJECTED", "CURRENT", "PAST", "FUTURE"};
        //---- проверка статусов в цикле
        List<List<BookingDtoOut>> listGetAllBookingsByUserId = new ArrayList<>();
        for (String s :
                states) {
            listGetAllBookingsByUserId.add(service.getAllBookingsByUserId(testUserId1, s));

        }

        for (List<BookingDtoOut> list :
                listGetAllBookingsByUserId) {
            assertThat(list, notNullValue());
        }
        //-------
        assertThrows(RuntimeException.class, () -> {
            service.getAllBookingsByUserIdPage(testUserId1, "ALL123", "0", "100");
        });
        List<Page<BookingDtoOut>> listGetAllBookingsByUserIdPage = new ArrayList<>();
        for (String s :
                states) {
            listGetAllBookingsByUserIdPage.add(service.getAllBookingsByUserIdPage(testUserId1, s,
                    "0", "100"));

        }

        for (Page<BookingDtoOut> list :
                listGetAllBookingsByUserIdPage) {
            assertThat(list, notNullValue());
        }

        ///---------------
        //---- проверка статусов в цикле
        assertThrows(RuntimeException.class, () -> {
            service.getAllBookingsByOwner(testUserId1, "ALL123");
        });
        List<List<BookingDtoOut>> listGetAllBookingsByOwner = new ArrayList<>();
        for (String s :
                states) {
            listGetAllBookingsByUserId.add(service.getAllBookingsByOwner(testUserId1, s));

        }

        for (List<BookingDtoOut> list :
                listGetAllBookingsByOwner) {
            assertThat(list, notNullValue());
        }
        ///---------------
        //---- проверка статусов в цикле
        assertThrows(RuntimeException.class, () -> {
            service.getAllBookingsByOwnerPage(testUserId1, "ALL123", "0", "100");
        });
        List<Page<BookingDtoOut>> listgetAllBookingsByOwnerPage = new ArrayList<>();
        for (String s :
                states) {
            listgetAllBookingsByOwnerPage.add(service.getAllBookingsByOwnerPage(testUserId1, s,
                    "0", "100"));

        }

        for (Page<BookingDtoOut> list :
                listgetAllBookingsByOwnerPage) {
            assertThat(list, notNullValue());
        }

        //-----
        BookingDtoInput finalBookingDto = bookingDto;
        assertThrows(NotFoundException.class, () -> {
            service.addBooking(-1, finalBookingDto);
        });
        assertThrows(NotFoundException.class, () -> {
            service.addBooking(testUserId1, finalBookingDto);
        });
        itemDto.setAvailable(Boolean.FALSE);
        itemService.patchItem(testUserId1, testItemId1, itemDto);
        assertThrows(BadRequestException.class, () -> {
            service.addBooking(testUserId2, finalBookingDto);
        });
        itemDto.setAvailable(Boolean.TRUE);
        itemService.patchItem(testUserId1, testItemId1, itemDto);

        //------------
        bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(50),
                LocalDateTime.now().plusDays(51), testItemId1);
        outB = service.addBooking(testUserId2, bookingDto);
        outB = service.patchBooking(outB.getId(), testUserId1, Boolean.TRUE);
        BookingDtoOut finalOutB = outB;
        assertThrows(BadRequestException.class, () -> {
            service.patchBooking(finalOutB.getId(), testUserId1, Boolean.TRUE);
        });
        //-------
        bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(60),
                LocalDateTime.now().plusDays(61), testItemId1);
        outB = service.addBooking(testUserId2, bookingDto);
        BookingDtoOut finalOutB1 = outB;
        assertThrows(NotFoundException.class, () -> {
            service.patchBooking(finalOutB1.getId(), testUserId1 + 1, Boolean.TRUE);
        });
        //-------
        bookingDto = new BookingDtoInput(0L, LocalDateTime.now().plusDays(70),
                LocalDateTime.now().plusDays(69), testItemId1);
        BookingDtoInput finalBookingDto1 = bookingDto;
        assertThrows(BadRequestException.class, () -> {
            service.addBooking(testUserId2, finalBookingDto1);
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
