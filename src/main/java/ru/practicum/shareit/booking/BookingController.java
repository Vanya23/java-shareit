package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService service;
    private final String headerUserId = "X-Sharer-User-Id";


//    Получение списка всех бронирований текущего пользователя. Эндпоинт — GET /bookings?state={state}.
//    Параметр state необязательный и по умолчанию равен ALL (англ. «все»). Также он может принимать значения CURRENT
//    (англ. «текущие»), **PAST** (англ. «завершённые»), FUTURE (англ. «будущие»), WAITING (англ. «ожидающие
//    подтверждения»), REJECTED (англ. «отклонённые»).
//    Бронирования должны возвращаться отсортированными по дате от более новых к более старым.


    @GetMapping
    public List<BookingDtoOut> getAllBookingsByUserId(@RequestHeader(headerUserId) long userId,
                                                      @RequestParam(defaultValue = "ALL") String state) {
        return service.getAllBookingsByUserId(userId, state);
    }

    @GetMapping(params = {"from", "size"})
    public Page<BookingDtoOut> getAllBookingsByUserIdPage(@RequestHeader(headerUserId) long userId,
                                                          @RequestParam(defaultValue = "ALL") String state,
                                                          @RequestParam(defaultValue = "") String from,
                                                          @RequestParam(defaultValue = "") String size) {
        return service.getAllBookingsByUserIdPage(userId, state, from, size);
    }

    //    Получение списка бронирований для всех вещей текущего пользователя. Эндпоинт —
//    GET /bookings/owner?state={state}. Этот запрос имеет смысл для владельца хотя бы
//    одной вещи. Работа параметра state аналогична его работе в предыдущем сценарии.
    @GetMapping(value = "/owner")
    public List<BookingDtoOut> getAllBookingsByOwner(@RequestHeader(headerUserId) long userId,
                                                     @RequestParam(defaultValue = "ALL") String state) {
        return service.getAllBookingsByOwner(userId, state);
    }

    @GetMapping(value = "/owner", params = {"from", "size"})
    public Page<BookingDtoOut> getAllBookingsByOwnerPage(@RequestHeader(headerUserId) long userId,
                                                         @RequestParam(defaultValue = "ALL") String state,
                                                         @RequestParam String from,
                                                         @RequestParam String size) {
        return service.getAllBookingsByOwnerPage(userId, state, from, size);
    }

    //    Получение данных о конкретном бронировании (включая его статус). Может быть выполнено либо автором бронирования,
//    либо владельцем вещи, к которой относится бронирование. Эндпоинт — GET /bookings/{bookingId}.
    @GetMapping("/{bookingId}")
    public BookingDtoOut getBookingById(@RequestHeader(headerUserId) long userId,
                                        @PathVariable long bookingId) {

        return service.getBookingById(bookingId, userId);
    }

    //    Запрос может быть создан любым пользователем, а затем подтверждён владельцем вещи. Эндпоинт — POST /bookings.
//    После создания запрос находится в статусе WAITING — «ожидает подтверждения».
    @PostMapping
    public BookingDtoOut addBooking(@RequestHeader(headerUserId) long userId,
                                    @Validated({Create.class}) @RequestBody BookingDtoInput booking) {
        return service.addBooking(userId, booking);
    }


    //    Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
//    Затем статус бронирования становится либо APPROVED, либо REJECTED. Эндпоинт —
//    PATCH /bookings/{bookingId}?approved={approved},
//    параметр approved может принимать значения true или false.
    @PatchMapping("/{bookingId}")
    public BookingDtoOut patchBooking(@PathVariable long bookingId,
                                      @RequestHeader(headerUserId) long userId,
                                      @RequestParam Boolean approved) {
        return service.patchBooking(bookingId, userId, approved);
    }


}
