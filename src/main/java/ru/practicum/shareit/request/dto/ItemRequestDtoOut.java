package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDtoOutForItemRequest;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDtoOut {

    Long id; // уникальный идентификатор запроса;
    String description; // текст запроса, содержащий описание требуемой вещи;
    String created; // дата и время создания запроса
    List<ItemDtoOutForItemRequest> items;
}
