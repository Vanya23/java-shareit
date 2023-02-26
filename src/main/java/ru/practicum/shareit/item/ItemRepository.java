package ru.practicum.shareit.item;

import ru.practicum.shareit.error.exception.IncorrectItemException;
import ru.practicum.shareit.error.exception.OtherOwnerItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item addItem(Item item);

    Item patchItem(ItemDto itemDto, long userId) throws IncorrectItemException, OtherOwnerItemException;

    Item getItemById(long itemId) throws IncorrectItemException;

    List<Item> getAllItemByUserId(long userId);

    List<Item> getAllItems();
}
