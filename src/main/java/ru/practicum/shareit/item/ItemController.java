package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;
    private final String headerUserId = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDtoOut> getAllItemByUserId(@RequestHeader(headerUserId) long userId) {
        return service.getAllItemByUserId(userId);
    }

    //    Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой. Эндпойнт
    @GetMapping("/{itemId}")
    public ItemDtoOut getItemById(@RequestHeader(headerUserId) long userId, @PathVariable long itemId) {
        return service.getItemById(itemId, userId);
    }

//    Просмотр информации о конкретной вещи по её идентификатору.
//    Эндпойнт GET /items/{itemId}. Информацию о вещи может просмотреть любой пользователь.

    @GetMapping("/search")
    public List<ItemDtoOut> searchItemByText(@RequestParam String text) {
        return service.searchItemByText(text);

    }

//    Поиск вещи потенциальным арендатором. Пользователь передаёт в строке запроса текст, и система ищет вещи,
//    содержащие этот текст в названии или описании. Происходит по эндпойнту /items/search?text={text},
//    в text передаётся текст для поиска. Проверьте, что поиск возвращает только доступные для аренды вещи.


    @PostMapping
    public ItemDtoOut addItem(@RequestHeader(headerUserId) long userId,
                              @Validated({Create.class}) @RequestBody ItemDtoIn itemDto) {
        return service.addItem(userId, itemDto);
    }
//    Добавление новой вещи. Будет происходить по эндпойнту POST /items. На вход поступает объект ItemDto.
//    userId в заголовке X-Sharer-User-Id — это идентификатор пользователя, который добавляет вещь.
//    Именно этот пользователь — владелец вещи. Идентификатор владельца будет поступать на вход в каждом из запросов, рассмотренных далее.

    @PatchMapping("/{itemId}")
    public ItemDtoOut patchItem(@RequestHeader(headerUserId) long userId, @PathVariable long itemId,
                                @Validated({Update.class}) @RequestBody ItemDtoIn itemDto) {
        return service.patchItem(userId, itemId, itemDto);
    }

    //    Редактирование вещи. Эндпойнт PATCH /items/{itemId}.
//    Изменить можно название, описание и статус доступа к аренде. Редактировать вещь может только её владелец.
    @PostMapping("/{itemId}/comment")
    public CommentDtoOut postComment(@RequestHeader(headerUserId) long userId, @PathVariable long itemId,
                                     @Validated({Create.class}) @RequestBody CommentDtoIn commentDto) {
        return service.postComment(userId, itemId, commentDto);
    }
//    Редактирование вещи. Эндпойнт PATCH /items/{itemId}.
//    Изменить можно название, описание и статус доступа к аренде. Редактировать вещь может только её владелец.


}
