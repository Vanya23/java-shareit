package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    int id;
    @NonNull
    String name;
    @NonNull
    String email;
}
