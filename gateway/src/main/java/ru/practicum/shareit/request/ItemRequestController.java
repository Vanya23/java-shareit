package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    private final String headerUserId = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(headerUserId) long userId,
                                                 @Validated({Create.class}) @RequestBody ItemRequestDtoIn itemRequestDtoIn) {
        return itemRequestClient.addItemRequest(userId, itemRequestDtoIn);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@RequestHeader(headerUserId) long userId) {

        return itemRequestClient.getAllByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getByRequestId(@RequestHeader(headerUserId) long userId, @PathVariable long requestId) {
        return itemRequestClient.getByRequestId(userId, requestId);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> getAllOtherUsers(@RequestHeader(headerUserId) long userId) {
        return itemRequestClient.getAllOtherUsers(userId);
    }

    @GetMapping(value = "/all", params = {"from", "size"})
    public ResponseEntity<Object> getAllOtherUsersPage(@RequestHeader(headerUserId) long userId,
                                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemRequestClient.getAllOtherUsersPage(userId, from, size);
    }


}
