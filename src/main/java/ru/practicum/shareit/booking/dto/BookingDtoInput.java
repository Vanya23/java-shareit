package ru.practicum.shareit.booking.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoInput {
    Long id;
    @NonNull
    String start; // дата и время начала бронирования;
    @NonNull
    String end; // дата и время конца бронирования;
    @NonNull
    Long itemId; // вещь, которую пользователь бронирует;
    @NonNull
    Long booker; //пользователь, который осуществляет бронирование;
    String status;
}
