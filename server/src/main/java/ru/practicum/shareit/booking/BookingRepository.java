package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker(User booker, Sort sort);

    List<Booking> findAllByBookerAndStatus(User booker, BookingStatus status, Sort sort);

    List<Booking> findAllByBookerAndEndBefore(User booker, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfter(User booker, LocalDateTime start,
                                                           LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerAndStartAfter(User booker, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemIn(List<Item> items, Sort sort);

    List<Booking> findAllByItemInAndStatus(List<Item> items, BookingStatus status, Sort sort);

    List<Booking> findAllByItemInAndEndBefore(List<Item> items, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemInAndStartBeforeAndEndAfter(List<Item> items, LocalDateTime start,
                                                           LocalDateTime end, Sort sort);

    List<Booking> findAllByItemInAndStartAfter(List<Item> items, LocalDateTime start, Sort sort);

    List<Booking> findAllByItem_IdInAndStatusAndStartLessThanEqual(Set<Long> itemId, BookingStatus status,
                                                                   LocalDateTime ltime, Sort sort);

    List<Booking> findAllByItem_IdInAndStatusAndStartAfter(Set<Long> itemId, BookingStatus status,
                                                           LocalDateTime ltime, Sort sort);


    List<Booking> findAllByItem_IdAndBooker_IdAndStatusAndEndBefore(Long itemId, Long bookerId,
                                                                    BookingStatus status, LocalDateTime callTime);


    Page<Booking> findAllByBooker(User booker, Pageable pageable);


    Page<Booking> findAllByItemIn(List<Item> items, Pageable pageable);

    Page<Booking> findAllByItemInAndStatus(List<Item> items, BookingStatus status, Pageable pageable);

    Page<Booking> findAllByItemInAndEndBefore(List<Item> items, LocalDateTime callTime, Pageable pageable);

    Page<Booking> findAllByItemInAndStartBeforeAndEndAfter(List<Item> items, LocalDateTime callTime, LocalDateTime callTime1, Pageable pageable);

    Page<Booking> findAllByItemInAndStartAfter(List<Item> items, LocalDateTime callTime, Pageable pageable);

    Page<Booking> findAllByBookerAndStatus(User booker, BookingStatus status, Pageable pageable);

    Page<Booking> findAllByBookerAndEndBefore(User booker, LocalDateTime callTime, Pageable pageable);

    Page<Booking> findAllByBookerAndStartBeforeAndEndAfter(User booker, LocalDateTime callTime, LocalDateTime callTime1, Pageable pageable);

    Page<Booking> findAllByBookerAndStartAfter(User booker, LocalDateTime callTime, Pageable pageable);
}