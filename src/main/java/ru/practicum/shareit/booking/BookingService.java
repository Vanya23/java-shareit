package ru.practicum.shareit.booking;


import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {

    List<BookingDtoOut> getAllBookingsByUserId(long userId, String state);

    List<BookingDtoOut> getAllBookingsByOwner(long userId, String state);

    BookingDtoOut getBookingById(long bookingId, long userId);

    BookingDtoOut addBooking(long userId, BookingDtoInput bookingDto);

    BookingDtoOut patchBooking(long bookingId, long userId, Boolean approved);

    List<BookingDtoOut> getAllBookingsByUserIdPage(long userId, String state, String from, String size);

    List<BookingDtoOut> getAllBookingsByOwnerPage(long userId, String state, String from, String size);
}
