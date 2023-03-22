package ru.practicum.shareit.booking.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoInput {
    Long id;
    @NotNull(groups = {Create.class})
    LocalDateTime start; // дата и время начала бронирования;
    @NotNull(groups = {Create.class})
    LocalDateTime end; // дата и время конца бронирования;
    @NotNull(groups = {Create.class})
    Long itemId; // вещь, которую пользователь бронирует;
//    Long booker; //пользователь, который осуществляет бронирование;
//    String status;
}
