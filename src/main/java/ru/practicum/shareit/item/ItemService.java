package ru.practicum.shareit.item;

import ru.practicum.shareit.error.exception.IncorrectIdUser;
import ru.practicum.shareit.error.exception.IncorrectIdUserInClassItem;
import ru.practicum.shareit.error.exception.IncorrectItemException;
import ru.practicum.shareit.error.exception.OtherOwnerItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto addItem(int userId, Item item) throws IncorrectIdUser, IncorrectItemException, IncorrectIdUserInClassItem;

    ItemDto patchItem(int userId, int itemId, Item item) throws IncorrectIdUser, IncorrectItemException, IncorrectIdUserInClassItem, OtherOwnerItemException;

    ItemDto getItemById(int itemId) throws IncorrectItemException;

    List<ItemDto> getAllItemByUserId(int userId) throws IncorrectIdUserInClassItem;

    List<ItemDto> searchItemByText(String text);
}
