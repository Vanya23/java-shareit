package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDtoOutputForItem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id; // уникальный идентификатор вещи;
    @NotBlank(groups = {Create.class})
    String name; // краткое название;
    @NotBlank(groups = {Create.class})
    String description; // развёрнутое описание;
    @NotNull(groups = {Create.class})
    Boolean available; // статус о том, доступна или нет вещь для аренды;
    Long request; // если вещь была создана по запросу другого пользователя, то в этом

    BookingDtoOutputForItem lastBooking; // информация последнего бронирования

    BookingDtoOutputForItem nextBooking; // информация ближайшего бронирования

    List<CommentDto> comments; // комментарии

}
