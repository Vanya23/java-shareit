package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    private final String headerUserId = "X-Sharer-User-Id";

    @GetMapping(params = {"from", "size"})
    public ResponseEntity<Object> getBookingsPage(@RequestHeader(headerUserId) long userId,
                                                  @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        checkPageParametr(from, size);
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookingsPage(userId, state, from, size);

    }

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(headerUserId) long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId);
        return bookingClient.getBookings(userId, state);
    }


//    @GetMapping(value = "/owner", params = {"state"})
    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByOwner(@RequestHeader(headerUserId) long userId,
                                                        @RequestParam(defaultValue = "ALL") String state) {
        BookingState stateB = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId);
        return bookingClient.getAllBookingsByOwner(userId, stateB);
    }

    @GetMapping(value = "/owner", params = {"state", "from", "size"})
    public ResponseEntity<Object> getAllBookingsByOwnerPage(@RequestHeader(headerUserId) long userId,
                                                            @RequestParam(name = "state", defaultValue = "all") String state,
                                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState stateB = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        checkPageParametr(from, size);
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getAllBookingsByOwnerPage(userId, stateB, from, size);

    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(headerUserId) long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader(headerUserId) long userId,
                                           @Validated({Create.class}) @RequestBody BookingDtoInput requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping(value = "/{bookingId}", params = {"approved"})
    public ResponseEntity<Object> patchBooking(@PathVariable long bookingId,
                                               @RequestHeader(headerUserId) long userId,
                                               @RequestParam Boolean approved) {
        return bookingClient.patchBooking(bookingId, userId, approved);
    }

    private void checkPageParametr(int from, int size) {
        if(from < 0 || size <= 0) throw new IllegalArgumentException("checkPageParametr");
    }

}
