package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;


@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class CommentDto {
    Long id; // уникальный идентификатор комментария;
    @NotBlank(groups = {Create.class})
    String text; // текст комментария;
    String authorName; // имя автора;
    String created; // время создания;


}

