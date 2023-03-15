package ru.practicum.shareit.item;

import ru.practicum.shareit.error.exception.IncorrectIdUser;
import ru.practicum.shareit.error.exception.IncorrectIdUserInClassItem;
import ru.practicum.shareit.error.exception.IncorrectItemException;
import ru.practicum.shareit.error.exception.OtherOwnerItemException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(long userId, ItemDto itemDto) throws IncorrectIdUser, IncorrectItemException, IncorrectIdUserInClassItem;

    ItemDto patchItem(long userId, long itemId, ItemDto itemDto) throws IncorrectIdUser, IncorrectItemException, IncorrectIdUserInClassItem, OtherOwnerItemException;

    ItemDto getItemById(long itemId) throws IncorrectItemException;

    List<ItemDto> getAllItemByUserId(long userId) throws IncorrectIdUserInClassItem;

    List<ItemDto> searchItemByText(String text);
}
