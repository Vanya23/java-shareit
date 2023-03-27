package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {


    List<ItemRequestDtoOut> getAllByUserId(long userId);

    ItemRequestDtoOut addItemRequest(long userId, ItemRequestDtoIn itemRequestDtoIn);


    List<ItemRequestDtoOut> getAllOtherUsersPage(long userId, String from, String size);

    ItemRequestDtoOut getByRequestId(long userId, long requestId);

    List<ItemRequestDtoOut> getAllOtherUsers(long userId);
}
