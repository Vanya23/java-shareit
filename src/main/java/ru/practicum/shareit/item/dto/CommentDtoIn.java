package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;


@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class CommentDtoIn {
    @NotBlank(groups = {Create.class})
    String text; // текст комментария;
}

