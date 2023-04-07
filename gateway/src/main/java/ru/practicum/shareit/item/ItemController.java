package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoIn;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemClient itemClient;

    private final String headerUserId = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAllItemByUserId(@RequestHeader(headerUserId) long userId) {

        return itemClient.getAllItemByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(headerUserId) long userId, @PathVariable long itemId) {

        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemByText(@RequestHeader(headerUserId) long userId,
                                                   @RequestParam String text) {

        return itemClient.searchItemByText(userId, text);
    }


    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(headerUserId) long userId,
                                          @Validated({Create.class}) @RequestBody ItemDtoIn itemDto) {

        return itemClient.addItem(userId, itemDto);
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patchItem(@RequestHeader(headerUserId) long userId, @PathVariable long itemId,
                                            @Validated({Update.class}) @RequestBody ItemDtoIn itemDto) {
        return itemClient.patchItem(userId, itemId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@RequestHeader(headerUserId) long userId, @PathVariable long itemId,
                                              @Validated({Create.class}) @RequestBody CommentDtoIn commentDto) {
        return itemClient.postComment(userId, itemId, commentDto);
    }


}
