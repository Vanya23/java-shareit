package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    ArrayList<Booking> findAllByBookerOrderByEndDesc(User booker);

    ArrayList<Booking> findAllByBookerAndStatusOrderByEndDesc(User booker, BookingStatus status);

    ArrayList<Booking> findAllByBookerAndEndBeforeOrderByEndDesc(User booker, LocalDateTime end);

    ArrayList<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByEndDesc(User booker, LocalDateTime start, LocalDateTime end);

    ArrayList<Booking> findAllByBookerAndStartAfterOrderByEndDesc(User booker, LocalDateTime start);

    ArrayList<Booking> findAllByItemInOrderByEndDesc(List<Item> items);

    ArrayList<Booking> findAllByItemInAndStatusOrderByEndDesc(List<Item> items, BookingStatus status);

    ArrayList<Booking> findAllByItemInAndEndBeforeOrderByEndDesc(List<Item> items, LocalDateTime end);

    ArrayList<Booking> findAllByItemInAndStartBeforeAndEndAfterOrderByEndDesc(List<Item> items, LocalDateTime start, LocalDateTime end);

    ArrayList<Booking> findAllByItemInAndStartAfterOrderByEndDesc(List<Item> items, LocalDateTime start);

    ArrayList<Booking> findAllByItemAndEndBeforeOrderByEndDesc(Item item, LocalDateTime ltime);

    ArrayList<Booking> findAllByItemAndStartAfterOrderByStartAsc(Item item, LocalDateTime ltime);

    Boolean existsByItem_Id(Long id);

    ArrayList<Booking> findAllByItem_IdAndBooker_IdAndStatusAndEndBefore(Long itemId, Long bookerId, BookingStatus status, LocalDateTime callTime);

}