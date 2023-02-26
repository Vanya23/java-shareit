package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
        itemDto.setRequest(item.getRequest() != null ? item.getRequest().getId() : null);
        return itemDto;
    }

    public static List<ItemDto> toListItemDto(List<Item> items) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item :
                items) {
            itemDtoList.add(toItemDto(item));
        }
        return itemDtoList;
    }


}
