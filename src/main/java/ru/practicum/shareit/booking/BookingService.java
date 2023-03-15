package ru.practicum.shareit.booking;


import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;

import java.util.List;

public interface BookingService {

    List<BookingDtoOutput> getAllBookingsByUserId(long userId, String state) throws Exception;

    List<BookingDtoOutput> getAllBookingsByOwner(long userId, String state) throws Exception;

    BookingDtoOutput getBookingById(long bookingId, long userId) throws NotFoundException;

    BookingDtoOutput addBooking(long userId, BookingDtoInput bookingDto) throws BadRequestException, NotFoundException;

    BookingDtoOutput patchBooking(long bookingId, long userId, Boolean approved) throws Exception;
}
