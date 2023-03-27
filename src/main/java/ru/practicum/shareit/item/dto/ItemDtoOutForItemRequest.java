package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoOutForItemRequest {
    Long id; // уникальный идентификатор вещи;
    String name; // краткое название;
    String description; // развёрнутое описание;
    Boolean available; // статус о том, доступна или нет вещь для аренды;
    Long requestId; // если вещь была создана по запросу другого пользователя, то в этом


}
