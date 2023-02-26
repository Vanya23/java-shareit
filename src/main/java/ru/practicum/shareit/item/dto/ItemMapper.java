package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {
    public ItemDto fromItemToItemDto(Item item) {
        ItemDto itemDto = new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null);
        return itemDto;
    }

    public List<ItemDto> fromListItemToListItemDto(List<Item> items) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item :
                items) {
            itemDtoList.add(fromItemToItemDto(item));
        }
        return itemDtoList;
    }


    public Item fromItemDtoToItem(ItemDto itemDto, long userId) {
        Item item = new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                userId,
                null);
        return item;
    }


}
