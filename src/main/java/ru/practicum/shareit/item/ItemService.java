package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.List;

public interface ItemService {
    ItemDtoOut addItem(long userId, ItemDtoIn itemDto)  ;

    ItemDtoOut patchItem(long userId, long itemId, ItemDtoIn itemDto)  ;

    ItemDtoOut getItemById(long itemId, long userId)  ;

    List<ItemDtoOut> getAllItemByUserId(long userId)  ;

    List<ItemDtoOut> searchItemByText(String text);

    CommentDtoOut postComment(long userId, long itemId, CommentDtoIn commentDto)  ;
}
