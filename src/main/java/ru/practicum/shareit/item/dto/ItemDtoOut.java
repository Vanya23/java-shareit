package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDtoOutputForItem;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoOut {
    Long id; // уникальный идентификатор вещи;
    String name; // краткое название;
    String description; // развёрнутое описание;
    Boolean available; // статус о том, доступна или нет вещь для аренды;
    Long request; // если вещь была создана по запросу другого пользователя, то в этом

    BookingDtoOutputForItem lastBooking; // информация последнего бронирования

    BookingDtoOutputForItem nextBooking; // информация ближайшего бронирования

    List<CommentDtoOut> comments; // комментарии

}
