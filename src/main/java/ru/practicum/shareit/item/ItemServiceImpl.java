package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.MyServicePage;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingPatternTime;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    ItemRepository repository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemRequestRepository itemRequestRepository;
    ItemMapper mapper;
    CommentMapper commentMapper;
    BookingPatternTime bookingPatternTime;

    MyServicePage myServicePage;
    Sort sortEndDesc = Sort.by(Sort.Direction.DESC, "end");
    Sort sortStartAsc = Sort.by(Sort.Direction.ASC, "start");
    Sort sortIdDesc = Sort.by(Sort.Direction.ASC, "id");

    @Override
    @Transactional
    public ItemDtoOut addItem(long userId, ItemDtoIn itemDto) {
        checkUserIdInItem(userId); // проверка существует ли user по id на исключение
        // Проверка объекта не выполняется т.к. сделана в @Validated
        Item item = mapper.fromItemDtoInToItem(itemDto, userId, userRepository, itemRequestRepository);
        checkUserIdInItem(userId); // чтобы не прокручивался счетчик в бд
        item = repository.save(item);
        return mapper.fromItemToItemDtoOut(item);
//        return itemMapper.fromItemToItemDto(repository.save(itemMapper.fromItemDtoToItem(itemDto, userId, userRepository)));
    }

    @Override
    @Transactional
    public ItemDtoOut patchItem(long userId, long itemId, ItemDtoIn itemDto) {
        checkUserIdInItem(userId); // проверка существует ли user по id на исключение
        itemDto.setId(itemId);
        // Передается ItemDto т.к. по условие на update может приходить не полный объект,
        // а объект Item не должен содержать нулевые значения
        Item itemForPatch = repository.getReferenceById(itemDto.getId());
        if (!(itemForPatch.getOwner().getId().equals(userId)))
            throw new NotFoundException("ItemServiceImpl -> patchItem -> OtherOwnerItemException");

        if (Strings.isNotBlank(itemDto.getName())) {
            itemForPatch.setName(itemDto.getName());
        }
        if (Strings.isNotBlank(itemDto.getDescription())) {
            itemForPatch.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemForPatch.setAvailable(itemDto.getAvailable());
        }
        return mapper.fromItemToItemDtoOut(itemForPatch);
    }

    @Override
    public ItemDtoOut getItemById(long itemId, long userId) {
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
        return mapper.fromItemToItemDtoOut(items.get(0));
    }

    @Override
    public List<ItemDtoOut> getAllItemByUserId(long userId) {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        checkUserIdInItem(userId); // проверка существует ли user по id на исключение
        List<Item> items = repository.findAllByOwner_Id(userId,
                sortIdDesc);
        addBookingTime(items, callTime); // добавление времени
        // добавление комментария
        addComment(items);
        return mapper.fromListItemToListItemDtoOut(items);
    }

    @Override
    public Page<ItemDtoOut> getAllItemByUserIdPage(long userId, String from, String size) {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        checkUserIdInItem(userId); // проверка существует ли user по id на исключение

        Pageable pageable = myServicePage.checkAndCreatePageable(from, size, sortIdDesc);

        Page<Item> page = repository.findAllByOwner_Id(userId, pageable);

        return helpPage(page, pageable, callTime);
    }

    @Override
    public List<ItemDtoOut> searchItemByText(String textForFind) {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        List<Item> items = repository.searchItemByText(textForFind);
        addBookingTime(items, callTime); // добавление времени
        // добавление комментария
        addComment(items);
        return mapper.fromListItemToListItemDtoOut(items);
    }


    @Override
    public Page<ItemDtoOut> searchItemByTextPage(String text, String from, String size) {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1); // не ставить точку оставки до этого момента
        Pageable pageable = myServicePage.checkAndCreatePageable(from, size, sortIdDesc);
        Page<Item> page = repository.searchItemByTextPage(text, pageable);
        return helpPage(page, pageable, callTime);
    }

    private Page<ItemDtoOut> helpPage(Page<Item> page, Pageable pageable, LocalDateTime callTime) {
        List<Item> items = page.getContent();
        addBookingTime(items, callTime); // добавление времени
        // добавление комментария
        addComment(items);
        // page mapping
        List<ItemDtoOut> itemDtoOuts = mapper.fromListItemToListItemDtoOut(items);
        Page<ItemDtoOut> pageOut = new PageImpl<>(itemDtoOuts, pageable, page.getTotalElements());
        return pageOut;
    }

    @Override
    @Transactional
    public CommentDtoOut postComment(long userId, long itemId, CommentDtoIn commentDto) {
        LocalDateTime callTime = LocalDateTime.parse(LocalDateTime.now().plusSeconds(1).format(bookingPatternTime.getFormatter())); // не ставить точку оставки до этого момента
        // проверка есть ли такая вещь и такой автор
        if (!(repository.existsById(itemId) && userRepository.existsById(userId)))
            throw new BadRequestException(getClass() + "  no exist user or item");
        // проверка брал ли пользователь вещь в аренду
        List<Booking> isWriteComment = bookingRepository.findAllByItem_IdAndBooker_IdAndStatusAndEndBefore(
                itemId, userId, BookingStatus.APPROVED, callTime);
        if (isWriteComment == null || isWriteComment.size() == 0)
            throw new BadRequestException(getClass() + " isWriteComment - no");

        Comment comment = commentMapper.fromCommentDtoInToComment(commentDto);
        comment.setCreated(callTime);
        comment.setItem(repository.getReferenceById(itemId));
        comment.setAuthor(userRepository.getReferenceById(userId));
        comment = commentRepository.save(comment);
        return commentMapper.fromCommentToCommentDtoOut(comment);
    }


    private void checkUserIdInItem(long userId) {
        if (!userRepository.existsUserById(userId))
            throw new NotFoundException("IncorrectIdUserInClassItem"); // чтобы не прокручивался счетчик в бд
    }

    private void addBookingTime(List<Item> items, LocalDateTime callTime) {
        // создание мапы items
        Map<Long, Item> mapItems = new HashMap<>();
        for (Item item :
                items) {
            mapItems.put(item.getId(), item);
        }

        List<Booking> allLastBookings = bookingRepository.findAllByItem_IdInAndStatusAndStartLessThanEqual(
                mapItems.keySet(), BookingStatus.APPROVED, callTime, sortEndDesc);
        List<Booking> allNextBookings = bookingRepository.findAllByItem_IdInAndStatusAndStartAfter(
                mapItems.keySet(), BookingStatus.APPROVED, callTime, sortStartAsc);
        // заполение мапы букингами
        for (Booking booking : allLastBookings) {
            long itemId = booking.getItem().getId();
            if (mapItems.get(itemId).getLastBooking() != null) continue;
            // т.к. нужный вариант для каждого Item будет первым, то после присвоения, новые значения не присваиваются
            mapItems.get(itemId).setLastBooking(booking);

        }
        for (Booking booking : allNextBookings) {
            long itemId = booking.getItem().getId();
            if (mapItems.get(itemId).getNextBooking() != null) continue;
            // т.к. нужный вариант для каждого Item будет первым, то после присвоения, новые значения не присваиваются
            mapItems.get(booking.getItem().getId()).setNextBooking(booking);
        }
    }

    private void addComment(List<Item> items) {
        // добавление комментария
        // создание мапы комментариев
        Map<Long, List<Comment>> mapComments = new HashMap<>();
        for (Item item :
                items) {
            mapComments.put(item.getId(), new ArrayList<>());
        }
        // получение комментариев для списка item
        List<Comment> allComments = commentRepository.findAllByItem_IdIn(mapComments.keySet(), sortIdDesc);
// заполение мапы комментариями
        for (Comment comment : allComments) {
            mapComments.get(comment.getItem().getId()).add(comment);
        }
        for (Item item :
                items) {
            item.setComments(mapComments.get(item.getId()));
        }
    }
}
