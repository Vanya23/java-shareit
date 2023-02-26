package ru.practicum.shareit.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;


@NoArgsConstructor
@RequiredArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {
    int id; // уникальный идентификатор запроса;
    @NonNull
    String description; // текст запроса, содержащий описание требуемой вещи;
    @NonNull
    User requestor; // пользователь, создавший запрос;
    @NonNull
    LocalDateTime created; // дата и время создания запроса.
}
