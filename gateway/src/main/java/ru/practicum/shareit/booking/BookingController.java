package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
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

	@GetMapping
	public ResponseEntity<Object> getBookingsPage(@RequestHeader(headerUserId) long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
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

	@GetMapping(value = "/owner")
	public ResponseEntity<Object> getAllBookingsByOwner(@RequestHeader(headerUserId) long userId,
													 @RequestParam(defaultValue = "ALL") String stateParam) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId);
		return bookingClient.getAllBookingsByOwner(userId, state);
	}

	@GetMapping(value = "/owner")
	public ResponseEntity<Object> getAllBookingsByOwnerPage(@RequestHeader(headerUserId) long userId,
												  @RequestParam(name = "state", defaultValue = "all") String stateParam,
												  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
												  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getAllBookingsByOwnerPage(userId, state, from, size);

	}




	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader(headerUserId) long userId,
			@PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}


	@PostMapping
	public ResponseEntity<Object> bookItem(@RequestHeader(headerUserId) long userId,
										   @RequestBody @Valid BookingDtoInput requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}




	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> patchBooking(@PathVariable long bookingId,
											   @RequestHeader(headerUserId) long userId,
											   @RequestParam Boolean approved) {
		log.info("Creating bookingId {}, userId={},  approved={}", bookingId, userId, approved);
		return bookingClient.patchBooking(bookingId, userId, approved);
	}



}
