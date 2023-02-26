package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.exception.IncorrectItemException;
import ru.practicum.shareit.item.dto.ItemDto;

@Component
public class CheckItemService {

    public boolean checkBlankName(ItemDto item) {
        return !(item.getName() == null || item.getName().equals(""));
    }

    public boolean checkBlankDescription(ItemDto item) {
        return !(item.getDescription() == null || item.getDescription().equals(""));
    }

    public boolean checkBlankAvailable(ItemDto item) {
        return !(item.getAvailable() == null);
    }

    public void checkAllItem(ItemDto item) throws IncorrectItemException {
        if (!checkBlankName(item)) throw new IncorrectItemException("IncorrectItemException");
        if (!checkBlankDescription(item)) throw new IncorrectItemException("IncorrectItemException");
        if (!checkBlankAvailable(item)) throw new IncorrectItemException("IncorrectItemException");
    }

}
