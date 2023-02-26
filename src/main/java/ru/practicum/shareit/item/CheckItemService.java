package ru.practicum.shareit.item;

import ru.practicum.shareit.error.exception.IncorrectItemException;
import ru.practicum.shareit.item.model.Item;

public class CheckItemService {

    public static boolean checkBlankName(Item item) {
        return !(item.getName() == null || item.getName().equals(""));
    }

    public static boolean checkBlankDescription(Item item) {
        return !(item.getDescription() == null || item.getDescription().equals(""));
    }

    public static boolean checkBlankAvailable(Item item) {
        return !(item.getAvailable() == null);
    }

    public static void checkAllItem(Item item) throws IncorrectItemException {
        if (!checkBlankName(item)) throw new IncorrectItemException("IncorrectItemException");
        if (!checkBlankDescription(item)) throw new IncorrectItemException("IncorrectItemException");
        if (!checkBlankAvailable(item)) throw new IncorrectItemException("IncorrectItemException");
    }

}
