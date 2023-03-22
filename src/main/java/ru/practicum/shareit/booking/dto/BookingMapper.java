package ru.practicum.shareit.booking.dto;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingPatternTime;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingMapper {
    ItemMapper itemMapper;
    UserMapper userMapper;

    BookingPatternTime bookingPatternTime;

    public BookingDtoInput fromBookingToBookingDtoInput(Booking booking) {
        return new BookingDtoInput(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId()
        );

    }

    public List<BookingDtoInput> fromListBookingToListBookingDtoInput(List<Booking> bookings) {
        List<BookingDtoInput> bookingDtoList = new ArrayList<>();
        for (Booking booking :
                bookings) {
            bookingDtoList.add(fromBookingToBookingDtoInput(booking));
        }
        return bookingDtoList;
    }

    public Booking fromBookingDtoInputToBooking(BookingDtoInput bookingDto, Long booker, BookingStatus status,DateTimeFormatter formatter,
                                                ItemRepository itemRepository, UserRepository userRepository) {
        return new Booking(
                bookingDto.getStart(),
                bookingDto.getEnd(),
                itemRepository.getReferenceById(bookingDto.getItemId()),
                userRepository.getReferenceById(booker),
                status
        );


    }

    public BookingDtoOutput fromBookingToBookingDtoOutput(Booking booking) {
        return new BookingDtoOutput(
                booking.getId(),
                booking.getStart().format(bookingPatternTime.getFormatter()),
                booking.getEnd().format(bookingPatternTime.getFormatter()),
                itemMapper.fromItemToItemDtoIn(booking.getItem()),
                userMapper.fromUserToUserDto(booking.getBooker()),
                booking.getStatus().name()
        );

    }

    public List<BookingDtoOutput> fromListBookingToListBookingDtoOutput(List<Booking> bookings) {
        List<BookingDtoOutput> bookingDtoOutputList = new ArrayList<>();
        for (Booking booking :
                bookings) {
            bookingDtoOutputList.add(fromBookingToBookingDtoOutput(booking));
        }
        return bookingDtoOutputList;
    }


}
