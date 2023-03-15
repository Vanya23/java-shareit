package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.error.exception.IncorrectItemException;
import ru.practicum.shareit.error.exception.OtherOwnerItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> map = new HashMap<>();
    private long id = 1;

    @Override
    public Item addItem(Item item) {
        item.setId(id++);
        map.put(item.getId(), item);
        return item;
    }

    @Override
    public Item patchItem(ItemDto itemDto, long userId) throws IncorrectItemException, OtherOwnerItemException {
        // Передается ItemDto т.к. по условие на update может приходить не полный объект,
        // а объект Item не должен содержать нулевые значения
        Item temp = getItemById(itemDto.getId());
        if (!(temp.getOwner().equals(userId))) throw new OtherOwnerItemException("OtherOwnerItemException");

        if (itemDto.getName() != null) {
            temp.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            temp.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            temp.setAvailable(itemDto.getAvailable());
        }
        return temp;
    }

    @Override
    public Item getItemById(long itemId) throws IncorrectItemException {
        if (!map.containsKey(itemId)) throw new IncorrectItemException("IncorrectItemException");
        return map.get(itemId);
    }

    @Override
    public List<Item> getAllItemByUserId(long userId) {
        List<Item> items = new ArrayList<>();
        for (Item itm :
                map.values()) {
            if (itm.getOwner() == userId) items.add(itm);
        }
        return items;
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(map.values());
    }
}
