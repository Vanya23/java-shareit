package ru.practicum.shareit.item;

import ru.practicum.shareit.error.exception.IncorrectItemException;
import ru.practicum.shareit.error.exception.OtherOwnerItemException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item addItem(Item item);

    Item patchItem(Item item) throws IncorrectItemException, OtherOwnerItemException;

    Item getItemById(int itemId) throws IncorrectItemException;

    List<Item> getAllItemByUserId(int userId);

    List<Item> getAllItems();
}
