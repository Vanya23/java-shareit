package ru.practicum.shareit.booking.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoOutputForItem {
    Long id;
    String start; // дата и время начала бронирования;
    String end; // дата и время конца бронирования;
    Long itemId; // вещь, которую пользователь бронирует;
    Long bookerId; //пользователь, который осуществляет бронирование;
    String status;
}
