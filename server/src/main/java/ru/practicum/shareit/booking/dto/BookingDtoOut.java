package ru.practicum.shareit.booking.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.user.dto.UserDto;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoOut {
    Long id;
    String start; // дата и время начала бронирования;
    String end; // дата и время конца бронирования;
    ItemDtoIn item; // вещь, которую пользователь бронирует;
    UserDto booker; //пользователь, который осуществляет бронирование;
    String status;
}
