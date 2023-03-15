package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    Long id; // уникальный идентификатор вещи;
    @NotBlank
    @NonNull
    String name; // краткое название;
    @NotBlank
    @NonNull
    String description; // развёрнутое описание;
    @NonNull
    Boolean available; // статус о том, доступна или нет вещь для аренды;
    @NonNull
    Long owner; // владелец вещи;
    ItemRequest request; // если вещь была создана по запросу другого пользователя, то в этом
//    поле будет храниться ссылка на соответствующий запрос.
}
