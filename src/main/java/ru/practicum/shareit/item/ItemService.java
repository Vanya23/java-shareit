package ru.practicum.shareit.item;

import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(long userId, ItemDto itemDto) throws NotFoundException;

    ItemDto patchItem(long userId, long itemId, ItemDto itemDto) throws NotFoundException;

    ItemDto getItemById(long itemId, long userId) throws NotFoundException;

    List<ItemDto> getAllItemByUserId(long userId) throws NotFoundException;

    List<ItemDto> searchItemByText(String text);

    CommentDto postComment(long userId, long itemId, CommentDto commentDto) throws BadRequestException;
}
