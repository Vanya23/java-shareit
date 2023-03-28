package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.BookingPatternTime;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemRequestMapper {

    BookingPatternTime bookingPatternTime;


    public ItemRequestDtoOut fromItemRequestToItemRequestDtoOut(ItemRequest itemRequest) {
        return new ItemRequestDtoOut(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated().format(bookingPatternTime.getFormatter()),
                new ArrayList<>());
    }

    public List<ItemRequestDtoOut> fromListItemRequestToListItemRequestDtoOut(List<ItemRequest> items) {
        List<ItemRequestDtoOut> itemDtoList = new ArrayList<>();
        for (ItemRequest item :
                items) {
            itemDtoList.add(fromItemRequestToItemRequestDtoOut(item));
        }
        return itemDtoList;
    }

    public ItemRequest fromItemRequestDtoInToItemRequest(ItemRequestDtoIn itemRequestDtoIn) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDtoIn.getDescription());
        return itemRequest;
    }


}
