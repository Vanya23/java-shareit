package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;

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
    public List<BookingDtoOutput> getAllBookingsByUserId(@RequestHeader(headerUserId) long userId,
                                                         @RequestParam(defaultValue = "ALL") String state) throws Exception {
        return service.getAllBookingsByUserId(userId, state);
    }

    //    Получение списка бронирований для всех вещей текущего пользователя. Эндпоинт —
//    GET /bookings/owner?state={state}. Этот запрос имеет смысл для владельца хотя бы
//    одной вещи. Работа параметра state аналогична его работе в предыдущем сценарии.
    @GetMapping("/owner")
    public List<BookingDtoOutput> getAllBookingsByOwner(@RequestHeader(headerUserId) long userId,
                                                        @RequestParam(defaultValue = "ALL") String state) throws Exception {
        return service.getAllBookingsByOwner(userId, state);
    }

    //    Получение данных о конкретном бронировании (включая его статус). Может быть выполнено либо автором бронирования,
//    либо владельцем вещи, к которой относится бронирование. Эндпоинт — GET /bookings/{bookingId}.
    @GetMapping("/{bookingId}")
    public BookingDtoOutput getBookingById(@RequestHeader(headerUserId) long userId,
                                           @PathVariable long bookingId) throws NotFoundException {

        return service.getBookingById(bookingId, userId);
    }

    //    Запрос может быть создан любым пользователем, а затем подтверждён владельцем вещи. Эндпоинт — POST /bookings.
//    После создания запрос находится в статусе WAITING — «ожидает подтверждения».
    @PostMapping
    public BookingDtoOutput addBooking(@RequestHeader(headerUserId) long userId, @RequestBody BookingDtoInput booking) throws BadRequestException, NotFoundException {
        return service.addBooking(userId, booking);
    }


    //    Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
//    Затем статус бронирования становится либо APPROVED, либо REJECTED. Эндпоинт —
//    PATCH /bookings/{bookingId}?approved={approved},
//    параметр approved может принимать значения true или false.
    @PatchMapping("/{bookingId}")
    public BookingDtoOutput patchBooking(@PathVariable long bookingId,
                                         @RequestHeader(headerUserId) long userId,
                                         @RequestParam Boolean approved) throws Exception {
        return service.patchBooking(bookingId, userId, approved);
    }


}
