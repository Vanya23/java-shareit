package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.error.exception.IncorrectItemException;
import ru.practicum.shareit.error.exception.OtherOwnerItemException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Integer, Item> map = new HashMap<>();
    private int id = 1;
    @Override
    public Item addItem(Item item) {
        item.setId(id++);
        map.put(item.getId(), item);
        return item;
    }

    @Override
    public Item patchItem(Item item) throws IncorrectItemException, OtherOwnerItemException {
        Item temp = getItemById(item.getId());
        if (item.getOwner() != temp.getOwner()) throw new OtherOwnerItemException("OtherOwnerItemException");
        if (item.getName() != null) {
            temp.setName(item.getName());
        }
        if (item.getDescription() != null) {
            temp.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            temp.setAvailable(item.getAvailable());
        }
        return temp;
    }

    @Override
    public Item getItemById(int itemId) throws IncorrectItemException {
        if (!map.containsKey(itemId)) throw new IncorrectItemException("IncorrectItemException");
        return map.get(itemId);
    }

    @Override
    public List<Item> getAllItemByUserId(int userId) {
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
