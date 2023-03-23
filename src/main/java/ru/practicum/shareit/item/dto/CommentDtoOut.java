package ru.practicum.shareit.item.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class CommentDtoOut {
    Long id; // уникальный идентификатор комментария;
    String text; // текст комментария;
    String authorName; // имя автора;
    String created; // время создания;
}

