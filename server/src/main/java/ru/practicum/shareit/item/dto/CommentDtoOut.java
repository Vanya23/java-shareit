package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentDtoOut {
    Long id; // уникальный идентификатор комментария;
    String text; // текст комментария;
    String authorName; // имя автора;
    String created; // время создания;
}

