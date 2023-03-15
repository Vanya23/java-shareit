package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.exception.BadRequestException;
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

    public void checkAllItem(ItemDto item) throws BadRequestException {
        if (!checkBlankName(item)) throw new BadRequestException("CheckItemService - checkBlankName");
        if (!checkBlankDescription(item)) throw new BadRequestException("CheckItemService - checkBlankDescription");
        if (!checkBlankAvailable(item)) throw new BadRequestException("CheckItemService - checkBlankAvailable");
    }

}
