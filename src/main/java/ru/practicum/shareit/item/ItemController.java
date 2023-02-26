package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.error.exception.IncorrectIdUser;
import ru.practicum.shareit.error.exception.IncorrectIdUserInClassItem;
import ru.practicum.shareit.error.exception.IncorrectItemException;
import ru.practicum.shareit.error.exception.OtherOwnerItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;
    private final String HEADER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDto> getAllItemByUserId(@RequestHeader("X-Sharer-User-Id") int userId) throws IncorrectIdUserInClassItem {
        return service.getAllItemByUserId(userId);
    }

    //    Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой. Эндпойнт
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) throws IncorrectItemException {
        return service.getItemById(itemId);
    }

//    Просмотр информации о конкретной вещи по её идентификатору.
//    Эндпойнт GET /items/{itemId}. Информацию о вещи может просмотреть любой пользователь.

    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestParam String text) {
        return service.searchItemByText(text);

    }

//    Поиск вещи потенциальным арендатором. Пользователь передаёт в строке запроса текст, и система ищет вещи,
//    содержащие этот текст в названии или описании. Происходит по эндпойнту /items/search?text={text},
//    в text передаётся текст для поиска. Проверьте, что поиск возвращает только доступные для аренды вещи.


    @PostMapping
    public ItemDto addItem(@RequestHeader(HEADER_USER_ID) int userId, @RequestBody Item item) throws IncorrectIdUser, IncorrectItemException, IncorrectIdUserInClassItem {
        return service.addItem(userId, item);
    }
//    Добавление новой вещи. Будет происходить по эндпойнту POST /items. На вход поступает объект ItemDto.
//    userId в заголовке X-Sharer-User-Id — это идентификатор пользователя, который добавляет вещь.
//    Именно этот пользователь — владелец вещи. Идентификатор владельца будет поступать на вход в каждом из запросов, рассмотренных далее.

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@RequestHeader(HEADER_USER_ID) int userId, @PathVariable int itemId, @RequestBody Item item) throws IncorrectItemException, IncorrectIdUserInClassItem, IncorrectIdUser, OtherOwnerItemException {
        return service.patchItem(userId, itemId, item);
    }
//    Редактирование вещи. Эндпойнт PATCH /items/{itemId}.
//    Изменить можно название, описание и статус доступа к аренде. Редактировать вещь может только её владелец.
}
