package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;


@NoArgsConstructor
@RequiredArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    int id;
    @NonNull
    String name;
    @NonNull
    String email;
}
