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
import ru.practicum.shareit.GeneratePageableObj;
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

    GeneratePageableObj myServicePage;
    Sort sortEndDesc = Sort.by(Sort.Direction.DESC, "end");
    Sort sortStartAsc = Sort.by(Sort.Direction.ASC, "start");
    Sort sortIdDesc = Sort.by(Sort.Direction.ASC, "id");

    @Override
    @Transactional
    public ItemDtoOut addItem(long userId, ItemDtoIn itemDto) {
        checkUserId(userId);
        Item item = mapper.fromItemDtoInToItem(itemDto, userId, userRepository, itemRequestRepository);
        checkUserId(userId); // чтобы не прокручивался счетчик в бд
        item = repository.save(item);
        return mapper.fromItemToItemDtoOut(item);
    }

    @Override
    @Transactional
    public ItemDtoOut patchItem(long userId, long itemId, ItemDtoIn itemDto) {
        checkUserId(userId);
        itemDto.setId(itemId);
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
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1);
        if (!repository.existsById(itemId))
            throw new NotFoundException(getClass() + " getItemById -> EntityNotFoundException");
        Item item = repository.getReferenceById(itemId);
        ArrayList<Item> items = new ArrayList<>();
        items.add(item);
        if (item.getOwner().getId().equals(userId)) {
            addBookingTime(items, callTime);
        }
        addComment(items);
        return mapper.fromItemToItemDtoOut(items.get(0));
    }

    @Override
    public List<ItemDtoOut> getAllItemByUserId(long userId) {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1);
        checkUserId(userId);
        List<Item> items = repository.findAllByOwner_Id(userId,
                sortIdDesc);
        addBookingTime(items, callTime);

        addComment(items);
        return mapper.fromListItemToListItemDtoOut(items);
    }

    @Override
    public List<ItemDtoOut> getAllItemByUserIdPage(long userId, String from, String size) {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1);
        checkUserId(userId);

        Pageable pageable = myServicePage.checkAndCreatePageable(from, size, sortIdDesc);

        Page<Item> page = repository.findAllByOwner_Id(userId, pageable);

        return helpPage(page, pageable, callTime).getContent();
    }

    @Override
    public List<ItemDtoOut> searchItemByText(String textForFind) {
        LocalDateTime callTime = LocalDateTime.now().minusSeconds(1);
        List<Item> items = repository.searchItemByText(textForFind);
        addBookingTime(items, callTime);
        addComment(items);
        return mapper.fromListItemToListItemDtoOut(items);
    }


    @Override
    public List<ItemDtoOut> searchItemByTextPage(String text, String from, String size) {
        Pageable pageable = myServicePage.checkAndCreatePageable(from, size, sortIdDesc);
        List<ItemDtoOut> itemDtoOuts = searchItemByText(text);
        Page<ItemDtoOut> pageOut = new PageImpl<>(itemDtoOuts, pageable, itemDtoOuts.size());
        return pageOut.getContent();
    }

    private Page<ItemDtoOut> helpPage(Page<Item> page, Pageable pageable, LocalDateTime callTime) {
        List<Item> items = page.getContent();
        addBookingTime(items, callTime);
        addComment(items);
        List<ItemDtoOut> itemDtoOuts = mapper.fromListItemToListItemDtoOut(items);
        Page<ItemDtoOut> pageOut = new PageImpl<>(itemDtoOuts, pageable, page.getTotalElements());
        return pageOut;
    }

    @Override
    @Transactional
    public CommentDtoOut postComment(long userId, long itemId, CommentDtoIn commentDto) {
        LocalDateTime callTime = LocalDateTime.parse(LocalDateTime.now().plusSeconds(1).format(bookingPatternTime.getFormatter())); // не ставить точку оставки до этого момента
        if (!(repository.existsById(itemId) && userRepository.existsById(userId)))
            throw new BadRequestException(getClass() + "  no exist user or item");
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


    private void checkUserId(long userId) {
        // проверка существует ли user по id на исключение
        if (!userRepository.existsUserById(userId))
            throw new NotFoundException("IncorrectIdUserInClassItem");
    }

    private void addBookingTime(List<Item> items, LocalDateTime callTime) {
        Map<Long, Item> mapItems = new HashMap<>();
        for (Item item :
                items) {
            mapItems.put(item.getId(), item);
        }
        List<Booking> allLastBookings12 = bookingRepository.findAll();
        List<Booking> allLastBookings = bookingRepository.findAllByItem_IdInAndStatusAndStartLessThanEqual(
                mapItems.keySet(), BookingStatus.APPROVED, callTime, sortEndDesc);
        List<Booking> allNextBookings = bookingRepository.findAllByItem_IdInAndStatusAndStartAfter(
                mapItems.keySet(), BookingStatus.APPROVED, callTime, sortStartAsc);
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
        List<Comment> allComments = commentRepository.findAllByItem_IdIn(mapComments.keySet(), sortIdDesc);
        for (Comment comment : allComments) {
            mapComments.get(comment.getItem().getId()).add(comment);
        }
        for (Item item :
                items) {
            item.setComments(mapComments.get(item.getId()));
        }
    }
}
