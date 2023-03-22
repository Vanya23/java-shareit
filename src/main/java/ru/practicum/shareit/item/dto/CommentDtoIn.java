package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class CommentDtoIn {
    Long id; // уникальный идентификатор комментария;
    @NotBlank(groups = {Create.class})
    String text; // текст комментария;
    String authorName; // имя автора;
    LocalDateTime created; // время создания;
}

