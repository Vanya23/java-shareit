package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exception.IncorrectIdUser;
import ru.practicum.shareit.error.exception.IncorrectIdUserInClassItem;
import ru.practicum.shareit.error.exception.IncorrectItemException;
import ru.practicum.shareit.error.exception.OtherOwnerItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    @Autowired
    private final ItemRepository repository;
    @Autowired
    private final UserService userService;

    @Override
    public ItemDto addItem(int userId, Item item) throws IncorrectItemException, IncorrectIdUserInClassItem {
        checkUserIdInItem(userId); // проверка существует ли user по id на исключение
        CheckItemService.checkAllItem(item);
        item.setOwner(userId);
        return ItemMapper.toItemDto(repository.addItem(item));
    }

    @Override
    public ItemDto patchItem(int userId, int itemId, Item item) throws IncorrectItemException, IncorrectIdUserInClassItem, OtherOwnerItemException {
        checkUserIdInItem(userId); // проверка существует ли user по id на исключение
        item.setOwner(userId);
        item.setId(itemId);
        return ItemMapper.toItemDto(repository.patchItem(item));
    }

    @Override
    public ItemDto getItemById(int itemId) throws IncorrectItemException {
        return ItemMapper.toItemDto(repository.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getAllItemByUserId(int userId) throws IncorrectIdUserInClassItem {
        checkUserIdInItem(userId); // проверка существует ли user по id на исключение
        return ItemMapper.toListItemDto(repository.getAllItemByUserId(userId));
    }

    @Override
    public List<ItemDto> searchItemByText(String text) {
        text = text.toLowerCase();
        List<Item> items = repository.getAllItems();
        List<Item> ans = new ArrayList<>();
        if (!text.equals("")) {
            for (Item itm :
                    items) {
                boolean isFind = itm.getAvailable() && (itm.getDescription().toLowerCase().contains(text)
                        || itm.getName().toLowerCase().contains(text));
                if (isFind) ans.add(itm);
            }
        }
        return ItemMapper.toListItemDto(ans);
    }

    private void checkUserIdInItem(int userId) throws IncorrectIdUserInClassItem {
        try {
            userService.getUserById(userId); // проверка user по id на исключение
        } catch (IncorrectIdUser e) {
            throw new IncorrectIdUserInClassItem("IncorrectIdUserInClassItem");
        }
    }
}
