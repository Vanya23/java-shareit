package ru.practicum.shareit.booking;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    BookingRepository repository;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingMapper bookingMapper;

    BookingPatternTime bookingPatternTime;
    Sort sortEndDesc = Sort.by(Sort.Direction.DESC, "end");
    Sort sortIdDesc = Sort.by(Sort.Direction.ASC, "id");


    @Override
    public List<BookingDtoOutput> getAllBookingsByUserId(long userId, String state) {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        BookingState bookingState = null;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {

            throw new RuntimeException("Unknown state: UNSUPPORTED_STATUS");
        }
        if (!userRepository.existsById(userId)) throw new NotFoundException(getClass() + " user Not Found");
        List<Booking> ans = null;
        User booker = userRepository.getReferenceById(userId);
        switch (bookingState) {
            case ALL:
                ans = repository.findAllByBooker(booker, sortEndDesc);
                break;
            case WAITING:
            case REJECTED:
                ans = repository.findAllByBookerAndStatus(booker, BookingStatus.valueOf(bookingState.name()), sortEndDesc);
                break;
            case PAST:
                ans = repository.findAllByBookerAndEndBefore(booker, callTime, sortEndDesc);
                break;
            case CURRENT:
                ans = repository.findAllByBookerAndStartBeforeAndEndAfter(booker, callTime, callTime, sortEndDesc);
                break;
            case FUTURE:
                ans = repository.findAllByBookerAndStartAfter(booker, callTime, sortEndDesc);
                break;

        }

        return bookingMapper.fromListBookingToListBookingDtoOutput(ans);
    }

    @Override
    public List<BookingDtoOutput> getAllBookingsByOwner(long userId, String state) {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        BookingState bookingState = null;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unknown state: UNSUPPORTED_STATUS");//********** this
        }
        if (!userRepository.existsById(userId)) throw new NotFoundException(getClass() + " user Not Found");
        List<Booking> ans = null;
        List<Item> items = itemRepository.findAllByOwner(userRepository.getReferenceById(userId),
                sortIdDesc);

        switch (bookingState) {
            case ALL:
                ans = repository.findAllByItemIn(items, sortEndDesc);
                break;
            case WAITING:
            case REJECTED:
                ans = repository.findAllByItemInAndStatus(items,
                        BookingStatus.valueOf(bookingState.name()), sortEndDesc);
                break;
            case PAST:
                ans = repository.findAllByItemInAndEndBefore(items, callTime, sortEndDesc);
                break;
            case CURRENT:
                ans = repository.findAllByItemInAndStartBeforeAndEndAfter(items, callTime, callTime, sortEndDesc);
                break;
            case FUTURE:
                ans = repository.findAllByItemInAndStartAfter(items, callTime, sortEndDesc);
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
    @Transactional
    public BookingDtoOutput addBooking(long userId, BookingDtoInput bookingDto) throws BadRequestException, NotFoundException {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку остановки до этого момента
        // проверка id пользователя и id item
        if (!(userRepository.existsUserById(userId) && itemRepository.existsById(bookingDto.getItemId())))
            throw new NotFoundException(getClass() + "addBooking -> item or user not exist");
        Booking booking = bookingMapper.fromBookingDtoInputToBooking(bookingDto, userId,
                BookingStatus.WAITING, bookingPatternTime.getFormatter(),
                itemRepository, userRepository);
        validateTime(booking, callTime); // валидация времени
        if (booking.getBooker().getId().equals(booking.getItem().getOwner().getId()))
            throw new NotFoundException("addBooking -> пользователь хочет свой лот взять в аренду");
        if (!booking.getItem().getAvailable())
            throw new BadRequestException(getClass() + "addBooking -> item not available");
        booking = repository.save(booking);
//        booking = repository.getReferenceById(booking.getId());
        return bookingMapper.fromBookingToBookingDtoOutput(booking);
    }

    @Override
    @Transactional
    public BookingDtoOutput patchBooking(long bookingId, long userId, Boolean approved) throws NotFoundException, BadRequestException {
        User owner = userRepository.getReferenceById(userId);
        Booking booking = repository.getReferenceById(bookingId);
        if (booking.getStatus().equals(BookingStatus.APPROVED))
            throw new BadRequestException("patchBooking -> already approved");
        if (!owner.getId().equals(booking.getItem().getOwner().getId()))
            throw new NotFoundException("inside patchBooking wrong owner");
        if (approved) booking.setStatus(BookingStatus.APPROVED);
        else booking.setStatus(BookingStatus.REJECTED);
//        booking = repository.saveAndFlush(booking);
        return bookingMapper.fromBookingToBookingDtoOutput(booking);
    }

    private void validateTime(Booking booking, LocalDateTime callTime) throws BadRequestException {

        if (booking.getStart().equals(booking.getEnd())) throw new BadRequestException(getClass() + "time equals");
        if (booking.getStart().isBefore(callTime)) throw new BadRequestException(getClass() + "not validateTime");
        if (booking.getStart().isAfter(booking.getEnd()))
            throw new BadRequestException(getClass() + "not validateTime");
    }
}
