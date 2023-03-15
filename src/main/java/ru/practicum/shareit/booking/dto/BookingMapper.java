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

import java.time.LocalDateTime;
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
                booking.getStart().format(bookingPatternTime.getFormatter()),
                booking.getEnd().format(bookingPatternTime.getFormatter()),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus().name()
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

    public Booking fromBookingDtoInputToBooking(BookingDtoInput bookingDto, DateTimeFormatter formatter,
                                                ItemRepository itemRepository, UserRepository userRepository) {
        return new Booking(
                LocalDateTime.parse(bookingDto.getStart(), formatter),
                LocalDateTime.parse(bookingDto.getEnd(), formatter),
                itemRepository.getReferenceById(bookingDto.getItemId()),
                userRepository.getReferenceById(bookingDto.getBooker()),
                BookingStatus.valueOf(bookingDto.getStatus())
        );


    }

    public BookingDtoOutput fromBookingToBookingDtoOutput(Booking booking) {
        return new BookingDtoOutput(
                booking.getId(),
                booking.getStart().format(bookingPatternTime.getFormatter()),
                booking.getEnd().format(bookingPatternTime.getFormatter()),
                itemMapper.fromItemToItemDto(booking.getItem()),
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
