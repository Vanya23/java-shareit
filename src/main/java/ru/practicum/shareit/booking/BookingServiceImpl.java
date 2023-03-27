package ru.practicum.shareit.booking;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.MyServicePage;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
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
    BookingMapper mapper;
    MyServicePage myServicePage;
    BookingPatternTime bookingPatternTime;
    Sort sortEndDesc = Sort.by(Sort.Direction.DESC, "end");
    Sort sortIdDesc = Sort.by(Sort.Direction.ASC, "id");


    @Override
    public List<BookingDtoOut> getAllBookingsByUserId(long userId, String state) {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        BookingState bookingState = null;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {

            throw new RuntimeException("Unknown state: UNSUPPORTED_STATUS");
        }
        if (!userRepository.existsById(userId)) throw new NotFoundException(getClass() + " user Not Found");
        List<Booking> ans = List.of();
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

        return mapper.fromListBookingToListBookingDtoOutput(ans);
    }

    @Override
    public List<BookingDtoOut> getAllBookingsByUserIdPage(long userId, String state, String from, String size) {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        Pageable pageable = myServicePage.checkAndCreatePageable(from, size, sortEndDesc);
        Pageable pageableBlank = myServicePage.getPageableBlank();
        BookingState bookingState = null;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {

            throw new RuntimeException("Unknown state: UNSUPPORTED_STATUS");
        }
        if (!userRepository.existsById(userId)) throw new NotFoundException(getClass() + " user Not Found");
        Page<Booking> page = new PageImpl<>(List.of(), pageableBlank, 0); // если запрос пустой;
        User booker = userRepository.getReferenceById(userId);

        switch (bookingState) {
            case ALL:

                page = repository.findAllByBooker(booker, pageable);
                break;
            case WAITING:
            case REJECTED:
                page = repository.findAllByBookerAndStatus(booker, BookingStatus.valueOf(bookingState.name()), pageable);
                break;
            case PAST:
                page = repository.findAllByBookerAndEndBefore(booker, callTime, pageable);
                break;
            case CURRENT:
                page = repository.findAllByBookerAndStartBeforeAndEndAfter(booker, callTime, callTime, pageable);
                break;
            case FUTURE:
                page = repository.findAllByBookerAndStartAfter(booker, callTime, pageable);
                break;

        }

        return helpPage(page, pageable).getContent();
    }

    private Page<BookingDtoOut> helpPage(Page<Booking> page, Pageable pageable) {
        List<Booking> bookings = page.getContent();
        List<BookingDtoOut> bookingDtoOutput = mapper.fromListBookingToListBookingDtoOutput(bookings);
        Page<BookingDtoOut> pageOut = new PageImpl<>(bookingDtoOutput, pageable, page.getTotalElements());
        return pageOut;
    }

    @Override
    public List<BookingDtoOut> getAllBookingsByOwner(long userId, String state) {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        BookingState bookingState = null;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unknown state: UNSUPPORTED_STATUS");
        }
        if (!userRepository.existsById(userId)) throw new NotFoundException(getClass() + " user Not Found");
        List<Booking> ans = List.of();
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
        return mapper.fromListBookingToListBookingDtoOutput(ans);
    }

    @Override
    public List<BookingDtoOut> getAllBookingsByOwnerPage(long userId, String state, String from, String size) {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        Pageable pageable = myServicePage.checkAndCreatePageable(from, size, sortEndDesc);
        Pageable pageableBlank = myServicePage.getPageableBlank();
        BookingState bookingState = null;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unknown state: UNSUPPORTED_STATUS");
        }
        if (!userRepository.existsById(userId)) throw new NotFoundException(getClass() + " user Not Found");
        Page<Booking> page = new PageImpl<>(List.of(), pageableBlank, 0); // если запрос пустой;

        List<Item> items = itemRepository.findAllByOwner(userRepository.getReferenceById(userId),
                sortIdDesc);

        switch (bookingState) {
            case ALL:
                page = repository.findAllByItemIn(items, pageable);
                break;
            case WAITING:
            case REJECTED:
                page = repository.findAllByItemInAndStatus(items,
                        BookingStatus.valueOf(bookingState.name()), pageable);
                break;
            case PAST:
                page = repository.findAllByItemInAndEndBefore(items, callTime, pageable);
                break;
            case CURRENT:
                page = repository.findAllByItemInAndStartBeforeAndEndAfter(items, callTime, callTime, pageable);
                break;
            case FUTURE:
                page = repository.findAllByItemInAndStartAfter(items, callTime, pageable);
                break;
        }
        return helpPage(page, pageable).getContent();
    }

    @Override
    public BookingDtoOut getBookingById(long bookingId, long userId) {
        if (!repository.existsById(bookingId)) throw new NotFoundException(getClass() + " getBookingById");
        Booking booking = repository.getReferenceById(bookingId);
        // проверка прав доступа
        boolean rule = booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId);
        // проверка наличия
        boolean bookingIsExist = repository.existsById(bookingId);
        if (!rule || !bookingIsExist) throw new NotFoundException(getClass() + " getBookingById");
        return mapper.fromBookingToBookingDtoOutput(booking);
    }

    @Override
    @Transactional
    public BookingDtoOut addBooking(long userId, BookingDtoInput bookingDto) {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку остановки до этого момента
        // проверка id пользователя и id item
        if (!(userRepository.existsUserById(userId) && itemRepository.existsById(bookingDto.getItemId())))
            throw new NotFoundException(getClass() + "addBooking -> item or user not exist");
        Booking booking = mapper.fromBookingDtoInputToBooking(bookingDto, userId,
                BookingStatus.WAITING, bookingPatternTime.getFormatter(),
                itemRepository, userRepository);
        validateTime(booking, callTime); // валидация времени
        if (booking.getBooker().getId().equals(booking.getItem().getOwner().getId()))
            throw new NotFoundException("addBooking -> пользователь хочет свой лот взять в аренду");
        if (!booking.getItem().getAvailable())
            throw new BadRequestException(getClass() + "addBooking -> item not available");
        booking = repository.save(booking);
        return mapper.fromBookingToBookingDtoOutput(booking);
    }

    @Override
    @Transactional
    public BookingDtoOut patchBooking(long bookingId, long userId, Boolean approved) {
        User owner = userRepository.getReferenceById(userId);
        Booking booking = repository.getReferenceById(bookingId);
        if (booking.getStatus().equals(BookingStatus.APPROVED))
            throw new BadRequestException("patchBooking -> already approved");
        if (!owner.getId().equals(booking.getItem().getOwner().getId()))
            throw new NotFoundException("inside patchBooking wrong owner");
        if (approved) booking.setStatus(BookingStatus.APPROVED);
        else booking.setStatus(BookingStatus.REJECTED);
        return mapper.fromBookingToBookingDtoOutput(booking);
    }


    private void validateTime(Booking booking, LocalDateTime callTime) {

        if (booking.getStart().equals(booking.getEnd())) throw new BadRequestException(getClass() + "time equals");
        if (booking.getStart().isBefore(callTime)) throw new BadRequestException(getClass() + "not validateTime");
        if (booking.getStart().isAfter(booking.getEnd()))
            throw new BadRequestException(getClass() + "not validateTime");
    }
}
