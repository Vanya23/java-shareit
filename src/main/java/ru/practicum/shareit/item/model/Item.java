package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    int id; // уникальный идентификатор вещи;
    @NonNull
    String name; // краткое название;
    @NonNull
    String description; // развёрнутое описание;
    @NonNull
    Boolean available; // статус о том, доступна или нет вещь для аренды;
    int owner; // владелец вещи;
    ItemRequest request; // если вещь была создана по запросу другого пользователя, то в этом
//    поле будет храниться ссылка на соответствующий запрос.
}
