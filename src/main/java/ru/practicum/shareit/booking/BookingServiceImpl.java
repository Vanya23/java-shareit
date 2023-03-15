package ru.practicum.shareit.booking;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingPatternTime;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {
    BookingRepository repository;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingMapper bookingMapper;

    BookingPatternTime bookingPatternTime;


    @Override
    public List<BookingDtoOutput> getAllBookingsByUserId(long userId, String state) throws Exception {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        BookingState bookingState = null;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {

            throw new Exception("Unknown state: UNSUPPORTED_STATUS");
        }
        if (!userRepository.existsById(userId)) throw new NotFoundException(getClass() + " user Not Found");
        List<Booking> ans = null;
        User booker = userRepository.getReferenceById(userId);
        switch (bookingState) {
            case ALL:
                ans = repository.findAllByBookerOrderByEndDesc(booker);
                break;
            case WAITING:
            case REJECTED:
                ans = repository.findAllByBookerAndStatusOrderByEndDesc(booker, BookingStatus.valueOf(bookingState.name()));
                break;
            case PAST:
                ans = repository.findAllByBookerAndEndBeforeOrderByEndDesc(booker, callTime);
                break;
            case CURRENT:
                ans = repository.findAllByBookerAndStartBeforeAndEndAfterOrderByEndDesc(booker, callTime, callTime);
                break;
            case FUTURE:
                ans = repository.findAllByBookerAndStartAfterOrderByEndDesc(booker, callTime);
                break;

        }

        return bookingMapper.fromListBookingToListBookingDtoOutput(ans);
    }

    @Override
    public List<BookingDtoOutput> getAllBookingsByOwner(long userId, String state) throws Exception {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        BookingState bookingState = null;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new Exception("Unknown state: UNSUPPORTED_STATUS");
        }
        if (!userRepository.existsById(userId)) throw new NotFoundException(getClass() + " user Not Found");
        List<Booking> ans = null;
        List<Item> items = itemRepository.findAllByOwnerOrderById(userRepository.getReferenceById(userId));

        switch (bookingState) {
            case ALL:
                ans = repository.findAllByItemInOrderByEndDesc(items);
                break;
            case WAITING:
            case REJECTED:
                ans = repository.findAllByItemInAndStatusOrderByEndDesc(items, BookingStatus.valueOf(bookingState.name()));
                break;
            case PAST:
                ans = repository.findAllByItemInAndEndBeforeOrderByEndDesc(items, callTime);
                break;
            case CURRENT:
                ans = repository.findAllByItemInAndStartBeforeAndEndAfterOrderByEndDesc(items, callTime, callTime);
                break;
            case FUTURE:
                ans = repository.findAllByItemInAndStartAfterOrderByEndDesc(items, callTime);
                break;
        }
        return bookingMapper.fromListBookingToListBookingDtoOutput(ans);
    }

    @Override
    public BookingDtoOutput getBookingById(long bookingId, long userId) throws NotFoundException {
        if (!repository.existsById(bookingId)) throw new NotFoundException(getClass() + " getBookingById");
        Booking booking = repository.getReferenceById(bookingId);
        // проверка прав доступа
        boolean rule = booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId);
        // проверка наличия
        boolean bookingIsExist = repository.existsById(bookingId);
        if (!rule || !bookingIsExist) throw new NotFoundException(getClass() + " getBookingById");
        return bookingMapper.fromBookingToBookingDtoOutput(booking);
    }

    @Override
    public BookingDtoOutput addBooking(long userId, BookingDtoInput bookingDto) throws BadRequestException, NotFoundException {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null)
            throw new BadRequestException(getClass() + "null time");
        bookingDto.setBooker(userId);
        bookingDto.setStatus(BookingStatus.WAITING.name()); // для нового booking назначается waiting
        // проверка id пользователя и id item
        if (!(userRepository.existsUserById(userId) && itemRepository.existsById(bookingDto.getItemId())))
            throw new NotFoundException(getClass() + "addBooking -> item or user not exist");
        Booking booking = bookingMapper.fromBookingDtoInputToBooking(bookingDto, bookingPatternTime.getFormatter(),
                itemRepository, userRepository);
        validateTime(booking, callTime); // валидация времени
        if (booking.getBooker().getId().equals(booking.getItem().getOwner().getId()))
            throw new NotFoundException("addBooking -> ползователь хочет свой лот взять в аренду");
        if (!booking.getItem().getAvailable())
            throw new BadRequestException(getClass() + "addBooking -> item not available");
        booking = repository.save(booking);
        booking = repository.getReferenceById(booking.getId());
        return bookingMapper.fromBookingToBookingDtoOutput(booking);
    }

    @Override
    public BookingDtoOutput patchBooking(long bookingId, long userId, Boolean approved) throws NotFoundException, BadRequestException {
        User owner = userRepository.getReferenceById(userId);
        Booking booking = repository.getReferenceById(bookingId);
        if (booking.getStatus().equals(BookingStatus.APPROVED))
            throw new BadRequestException("patchBooking -> already approved");
        if (!owner.getId().equals(booking.getItem().getOwner().getId()))
            throw new NotFoundException("inside patchBooking wrong owner");
        if (approved) booking.setStatus(BookingStatus.APPROVED);
        else booking.setStatus(BookingStatus.REJECTED);
        booking = repository.saveAndFlush(booking);
        return bookingMapper.fromBookingToBookingDtoOutput(booking);
    }

    private void validateTime(Booking booking, LocalDateTime callTime) throws BadRequestException {

        if (booking.getStart().equals(booking.getEnd())) throw new BadRequestException(getClass() + "time equals");
        if (booking.getStart().isBefore(callTime)) throw new BadRequestException(getClass() + "not validateTime");
        if (booking.getStart().isAfter(booking.getEnd()))
            throw new BadRequestException(getClass() + "not validateTime");
    }
}
