package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exception.IncorrectIdUser;
import ru.practicum.shareit.error.exception.IncorrectIdUserInClassItem;
import ru.practicum.shareit.error.exception.IncorrectItemException;
import ru.practicum.shareit.error.exception.OtherOwnerItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) throws IncorrectIdUserInClassItem {
        checkUserIdInItem(userId); // проверка существует ли user по id на исключение
        // Проверка объекта не выполняется т.к. сделана в @Validated
        return itemMapper.fromItemToItemDto(repository.addItem(itemMapper.fromItemDtoToItem(itemDto, userId)));
    }

    @Override
    public ItemDto patchItem(long userId, long itemId, ItemDto itemDto) throws IncorrectItemException, IncorrectIdUserInClassItem, OtherOwnerItemException {
        checkUserIdInItem(userId); // проверка существует ли user по id на исключение
        itemDto.setId(itemId);
        return itemMapper.fromItemToItemDto(repository.patchItem(itemDto, userId));
    }

    @Override
    public ItemDto getItemById(long itemId) throws IncorrectItemException {
        return itemMapper.fromItemToItemDto(repository.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getAllItemByUserId(long userId) throws IncorrectIdUserInClassItem {
        checkUserIdInItem(userId); // проверка существует ли user по id на исключение
        return itemMapper.fromListItemToListItemDto(repository.getAllItemByUserId(userId));
    }

    @Override
    public List<ItemDto> searchItemByText(String textForFind) {
        textForFind = textForFind.toLowerCase();
        List<Item> items = repository.getAllItems();
        List<Item> ans = new ArrayList<>();
        if (!textForFind.equals("")) {
            for (Item itm :
                    items) {
                boolean isFind = itm.getAvailable() && (itm.getDescription().toLowerCase().contains(textForFind)
                        || itm.getName().toLowerCase().contains(textForFind));
                if (isFind) ans.add(itm);
            }
        }
        return itemMapper.fromListItemToListItemDto(ans);
    }

    private void checkUserIdInItem(long userId) throws IncorrectIdUserInClassItem {
        try {
            userRepository.getUserById(userId); // проверка user по id на исключение
        } catch (IncorrectIdUser e) {
            throw new IncorrectIdUserInClassItem("IncorrectIdUserInClassItem");
        }
    }
}
