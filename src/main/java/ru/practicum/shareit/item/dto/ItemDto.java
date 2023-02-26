package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    @NonNull
    int id; // уникальный идентификатор вещи;
    @NonNull
    String name; // краткое название;
    @NonNull
    String description; // развёрнутое описание;
    @NonNull
    Boolean available; // статус о том, доступна или нет вещь для аренды;
    Integer request; // если вещь была создана по запросу другого пользователя, то в этом

//    поле будет храниться ссылка на соответствующий запрос.
}
