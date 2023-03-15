package ru.practicum.shareit.booking.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoOutput {
    Long id;
    String start; // дата и время начала бронирования;
    String end; // дата и время конца бронирования;
    ItemDto item; // вещь, которую пользователь бронирует;
    UserDto booker; //пользователь, который осуществляет бронирование;
    String status;
}
