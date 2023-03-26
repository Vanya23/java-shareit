package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService service;
    private final String headerUserId = "X-Sharer-User-Id";


    @PostMapping
    public ItemRequestDtoOut addItemRequest(@RequestHeader(headerUserId) long userId,
                                            @Validated({Create.class}) @RequestBody ItemRequestDtoIn ItemRequestDtoIn) {
        return service.addItemRequest(userId, ItemRequestDtoIn);
    }

    //    добавить новый запрос вещи. Основная часть запроса — текст запроса, где пользователь описывает,
//    какая именно вещь ему нужна
    @GetMapping
    public List<ItemRequestDtoOut> getAllByUserId(@RequestHeader(headerUserId) long userId) {
        return service.getAllByUserId(userId);
    }
//    получить список своих запросов вместе с данными об ответах на них.
//    Для каждого запроса должны указываться описание, дата и время создания и список ответов в формате:
//    id вещи, название, id владельца. Так в дальнейшем, используя указанные id вещей, можно будет получить
//    подробную информацию о каждой вещи. Запросы должны возвращаться в отсортированном порядке от более новых к более старым.


    @GetMapping("/{requestId}")
    public ItemRequestDtoOut getByRequestId(@RequestHeader(headerUserId) long userId, @PathVariable long requestId) {
        return service.getByRequestId(userId, requestId);
    }

//    GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об ответах на него в том же формате,
//    что и в эндпоинте GET /requests. Посмотреть данные об отдельном запросе может любой пользователь.


    @GetMapping(value = "/all")
    public List<ItemRequestDtoOut> getAllOtherUsersPage(@RequestHeader(headerUserId) long userId) {
        return service.getAllOtherUsers(userId);
    }

    @GetMapping(value = "/all", params = {"from", "size"})
    public Page<ItemRequestDtoOut> getAllOtherUsersPage(@RequestHeader(headerUserId) long userId,
                                                        @RequestParam String from,
                                                        @RequestParam String size) {
        return service.getAllOtherUsersPage(userId, from, size);
    }

//    получить список запросов, созданных другими пользователями. С помощью этого эндпоинта пользователи смогут
//    просматривать существующие запросы, на которые они могли бы ответить. Запросы сортируются по дате создания:
//    от более новых к более старым. Результаты должны возвращаться постранично. Для этого нужно передать два параметра:
//    from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.


}
