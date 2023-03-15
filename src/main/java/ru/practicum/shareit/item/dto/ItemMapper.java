package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoOutputForItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemMapper {
    CommentMapper commentMapper;

    public ItemDto fromItemToItemDto(Item item) {
        ItemDto itemDto = new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                item.getLastBooking() != null ? fromBookingToBookingDtoOutputForItem(item.getLastBooking()) : null,
                item.getNextBooking() != null ? fromBookingToBookingDtoOutputForItem(item.getNextBooking()) : null,
                item.getComments() != null ? commentMapper.fromListCommentToCommentDto(item.getComments()) : null
        );
        return itemDto;
    }

    public List<ItemDto> fromListItemToListItemDto(List<Item> items) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item :
                items) {
            itemDtoList.add(fromItemToItemDto(item));
        }
        return itemDtoList;
    }


    public Item fromItemDtoToItem(ItemDto itemDto, long userId, UserRepository userRepository) {
        Item item = new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                userRepository.getReferenceById(userId),
                null, null, null, null);
        return item;
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


}
