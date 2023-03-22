package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoIn {
    Long id; // уникальный идентификатор вещи;
    @NotBlank(groups = {Create.class})
    String name; // краткое название;
    @NotBlank(groups = {Create.class})
    String description; // развёрнутое описание;
    @NotNull(groups = {Create.class})
    Boolean available; // статус о том, доступна или нет вещь для аренды;
}
