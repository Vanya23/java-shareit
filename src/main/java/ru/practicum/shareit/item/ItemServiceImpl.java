package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingPatternTime;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImpl implements ItemService {
    ItemRepository repository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemMapper itemMapper;
    CommentMapper commentMapper;
    BookingPatternTime bookingPatternTime;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) throws NotFoundException {
        checkUserIdInItem(userId); // проверка существует ли user по id на исключение
        // Проверка объекта не выполняется т.к. сделана в @Validated
        Item item = itemMapper.fromItemDtoToItem(itemDto, userId, userRepository);
        checkUserIdInItem(userId); // чтобы не прокручивался счетчик в бд
        item = repository.save(item);
        return itemMapper.fromItemToItemDto(item);
//        return itemMapper.fromItemToItemDto(repository.save(itemMapper.fromItemDtoToItem(itemDto, userId, userRepository)));
    }

    @Override
    public ItemDto patchItem(long userId, long itemId, ItemDto itemDto) throws NotFoundException {
        checkUserIdInItem(userId); // проверка существует ли user по id на исключение
        itemDto.setId(itemId);
        // Передается ItemDto т.к. по условие на update может приходить не полный объект,
        // а объект Item не должен содержать нулевые значения
        Item temp = repository.getReferenceById(itemDto.getId());
        if (!(temp.getOwner().getId().equals(userId)))
            throw new NotFoundException("ItemServiceImpl -> patchItem -> OtherOwnerItemException");

        if (itemDto.getName() != null) {
            temp.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            temp.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            temp.setAvailable(itemDto.getAvailable());
        }
        repository.saveAndFlush(temp); // обновление базы
        return itemMapper.fromItemToItemDto(repository.getReferenceById(itemDto.getId()));
    }

    @Override
    public ItemDto getItemById(long itemId, long userId) throws NotFoundException {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        if (!repository.existsById(itemId))
            throw new NotFoundException(getClass() + " getItemById -> EntityNotFoundException");
        Item item = repository.getReferenceById(itemId);
        ArrayList<Item> items = new ArrayList<>();
        items.add(item);
        if (item.getOwner().getId().equals(userId)) { //
//                чтобы владелец видел даты последнего и ближайшего следующего
//                бронирования для каждой вещи, когда просматривает список
            addBookingTime(items, callTime); // добавление времени
        }
        // добавление комментария
        addComment(items);
        return itemMapper.fromItemToItemDto(items.get(0));
    }

    @Override
    public List<ItemDto> getAllItemByUserId(long userId) throws NotFoundException {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        checkUserIdInItem(userId); // проверка существует ли user по id на исключение
        List<Item> items = repository.findAllByOwnerOrderById(userRepository.getReferenceById(userId));
        addBookingTime(items, callTime); // добавление времени
        // добавление комментария
        addComment(items);
        return itemMapper.fromListItemToListItemDto(items);
//        return itemMapper.fromListItemToListItemDto(repository.findAllByOwner(userId));
    }

    @Override
    public List<ItemDto> searchItemByText(String textForFind) {
        textForFind = textForFind.toLowerCase();
        List<Item> items = repository.findAll();
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

    @Override
    public CommentDto postComment(long userId, long itemId, CommentDto commentDto) throws BadRequestException {
        LocalDateTime callTime = LocalDateTime.parse(LocalDateTime.now().format(bookingPatternTime.getFormatter())); // не ставить точку оставки до этого момента
        // проверка есть ли такая вещь и такой автор
        if (!(repository.existsById(itemId) && userRepository.existsById(userId)))
            throw new BadRequestException(getClass() + "  no exist user or item");
        // проверка брал ли пользователь вещь в аренду
        List<Booking> isWriteComment = bookingRepository.findAllByItem_IdAndBooker_IdAndStatusAndEndBefore(
                itemId, userId, BookingStatus.APPROVED, callTime);
        if (isWriteComment == null || isWriteComment.size() == 0)
            throw new BadRequestException(getClass() + " isWriteComment - no");

        Comment comment = commentMapper.fromCommentDtoToComment(commentDto);
        comment.setCreated(callTime);
        comment.setItem(repository.getReferenceById(itemId));
        comment.setAuthor(userRepository.getReferenceById(userId));
        comment = commentRepository.saveAndFlush(comment);
        return commentMapper.fromCommentToCommentDto(comment);
    }

    private void checkUserIdInItem(long userId) throws NotFoundException {
//        userRepository.getReferenceById(userId); // проверка user по id на исключение
        if (!userRepository.existsUserById(userId))
            throw new NotFoundException("IncorrectIdUserInClassItem"); // чтобы не прокручивался счетчик в бд
    }

    void addBookingTime(List<Item> items, LocalDateTime callTime) {
        for (Item item :
                items) {
            if (bookingRepository.existsByItem_Id(item.getId())) {
                ArrayList<Booking> lastBookings = bookingRepository.findAllByItemAndEndBeforeOrderByEndDesc(item, callTime);
                ArrayList<Booking> nextBookings = bookingRepository.findAllByItemAndStartAfterOrderByStartAsc(item, callTime);
                if (lastBookings != null && lastBookings.size() > 0)
                    item.setLastBooking(lastBookings.get(0));
                if (nextBookings != null && nextBookings.size() > 0)
                    item.setNextBooking(nextBookings.get(0));
            }
        }
    }

    void addComment(List<Item> items) {
        // добавление комментария
        for (Item item :
                items) {
            List<Comment> comments = commentRepository.findAllByItem_IdOrderById(item.getId());
            item.setComments(comments);
        }
    }
}
