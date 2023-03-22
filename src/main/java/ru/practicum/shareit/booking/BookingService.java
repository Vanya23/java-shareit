package ru.practicum.shareit.booking;


import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;

import java.util.List;

public interface BookingService {

    List<BookingDtoOutput> getAllBookingsByUserId(long userId, String state)  ;

    List<BookingDtoOutput> getAllBookingsByOwner(long userId, String state)  ;

    BookingDtoOutput getBookingById(long bookingId, long userId)  ;

    BookingDtoOutput addBooking(long userId, BookingDtoInput bookingDto)  ;

    BookingDtoOutput patchBooking(long bookingId, long userId, Boolean approved)  ;
}
