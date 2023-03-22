package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

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

    List<Booking> findAllByItem_IdInAndStatusAndStartBefore(List<Long> itemId, BookingStatus status,
                                                            LocalDateTime ltime, Sort sort);

    List<Booking> findAllByItem_IdInAndStatusAndStartAfter(List<Long> itemId, BookingStatus status,
                                                           LocalDateTime ltime, Sort sort);

    Boolean existsByItem_Id(Long id);

    List<Booking> findAllByItem_IdAndBooker_IdAndStatusAndEndBefore(Long itemId, Long bookerId,
                                                                    BookingStatus status, LocalDateTime callTime);

}