package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoOutputForItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemMapper {
    CommentMapper commentMapper;

    public ItemDtoIn fromItemToItemDtoIn(Item item) {
        return new ItemDtoIn(
                item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );

    }


    public Item fromItemDtoInToItem(ItemDtoIn itemDto, long userId, UserRepository userRepository,
                                    ItemRequestRepository itemRequestRepository) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                userRepository.getReferenceById(userId),
                itemDto.getRequestId() != null ? itemRequestRepository.getReferenceById(itemDto.getRequestId()) : null,
                null, null, null);

    }


    public ItemDtoOut fromItemToItemDtoOut(Item item) {
        return new ItemDtoOut(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                item.getLastBooking() != null ? fromBookingToBookingDtoOutputForItem(item.getLastBooking()) : null,
                item.getNextBooking() != null ? fromBookingToBookingDtoOutputForItem(item.getNextBooking()) : null,
                item.getComments() != null ? commentMapper.fromListCommentToCommentDtoOut(item.getComments()) : null
        );

    }

    public List<ItemDtoOut> fromListItemToListItemDtoOut(List<Item> items) {
        List<ItemDtoOut> itemDtoList = new ArrayList<>();
        for (Item item :
                items) {
            itemDtoList.add(fromItemToItemDtoOut(item));
        }
        return itemDtoList;
    }

    public BookingDtoOutputForItem fromBookingToBookingDtoOutputForItem(Booking booking) {
        return new BookingDtoOutputForItem(
                booking.getId(),
                booking.getStart().toString(),
                booking.getEnd().toString(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus().name()
        );

    }

    public ItemDtoOutForItemRequest fromItemToItemDtoOutForItemRequest(Item item) {
        return new ItemDtoOutForItemRequest(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest().getId()
        );
    }

    public List<ItemDtoOutForItemRequest> fromListItemToListDtoOutForItemRequest(List<Item> items) {
        List<ItemDtoOutForItemRequest> itemDtoList = new ArrayList<>();
        for (Item item :
                items) {
            itemDtoList.add(fromItemToItemDtoOutForItemRequest(item));
        }
        return itemDtoList;
    }


}
