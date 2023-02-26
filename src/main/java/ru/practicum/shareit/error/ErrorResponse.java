package ru.practicum.shareit.error;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    @NonNull
    String error;
}
