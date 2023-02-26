package ru.practicum.shareit.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;


@NoArgsConstructor
@RequiredArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    int id;  // уникальный идентификатор бронирования;
    @NonNull
    LocalDateTime start; // дата и время начала бронирования;
    @NonNull
    LocalDateTime end; // дата и время конца бронирования;
    @NonNull
    Item item; // вещь, которую пользователь бронирует;
    @NonNull
    User booker; //пользователь, который осуществляет бронирование;
    @NonNull
    BookingStatus status;
}
