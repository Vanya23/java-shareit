package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.MyServicePage;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    ItemRequestRepository repository;
    ItemRepository itemRepository;
    UserRepository userRepository;
    ItemMapper itemMapper;
    ItemRequestMapper mapper;
    MyServicePage myServicePage;
    Sort sortCreatedDesc = Sort.by(Sort.Direction.DESC, "created");
    Sort sortCreatedAsc = Sort.by(Sort.Direction.ASC, "created");

    @Override
    public List<ItemRequestDtoOut> getAllByUserId(long userId) {
        checkUserIdInIItemRequest(userId); // проверка существует ли user по id на исключение
        List<ItemRequest> itemRequests = repository.findAllByRequestor_Id(userId, sortCreatedDesc);

        List<Item> items = itemRepository.findAllByRequestIn(itemRequests); // получили все вещи по имеющимся запросам
        // далее делаем мапу номер запроса и номера соотв вещей
        Map<Long, List<Item>> map = new HashMap<>();
        for (Item item :
                items) {
            long reqId = item.getRequest().getId();
            if (!map.containsKey(reqId)) map.put(reqId, new ArrayList<>());
            map.get(reqId).add(item);
        }

        List<ItemRequestDtoOut> itemRequestsList = mapper.fromListItemRequestToListItemRequestDtoOut(itemRequests);
        // заполняем запросы
        for (ItemRequestDtoOut itemRequestDtoOut :
                itemRequestsList) {
            long itemReqId = itemRequestDtoOut.getId();
            if (map.containsKey(itemReqId))
                itemRequestDtoOut.setItems(
                        itemMapper.fromListItemToListDtoOutForItemRequest(map.get(itemReqId)));
        }

        return itemRequestsList;
    }

    @Override
    @Transactional
    public ItemRequestDtoOut addItemRequest(long userId, ItemRequestDtoIn itemRequestDtoIn) {
        checkUserIdInIItemRequest(userId); // проверка существует ли user по id на исключение
        ItemRequest itemRequest = mapper.fromItemRequestDtoInToItemRequest(itemRequestDtoIn); // начало маппинга
        itemRequest.setRequestor(userRepository.getReferenceById(userId)); // конец маппинга
        repository.save(itemRequest);
        ItemRequestDtoOut itemRequestDtoOut = mapper.fromItemRequestToItemRequestDtoOut(itemRequest);
        return itemRequestDtoOut;
    }

    @Override
    public List<ItemRequestDtoOut> getAllOtherUsers(long userId) {
        checkUserIdInIItemRequest(userId); // проверка существует ли user по id на исключение
        List<ItemRequest> itemRequests = repository.findAllByRequestor_IdIsNot(userId, sortCreatedDesc);
        return mapper.fromListItemRequestToListItemRequestDtoOut(itemRequests);
    }

    @Override
    public Page<ItemRequestDtoOut> getAllOtherUsersPage(long userId, String from, String size) {
        checkUserIdInIItemRequest(userId); // проверка существует ли user по id на исключение

        Pageable pageable = myServicePage.checkAndCreatePageable(from, size, sortCreatedDesc);

        Page<ItemRequest> page = repository.findAllByRequestor_IdIsNot(userId, pageable);

        // page mapping
        List<ItemRequestDtoOut> itemRequestDtoOuts = mapper.fromListItemRequestToListItemRequestDtoOut(page.getContent());
        Page<ItemRequestDtoOut> pageOut = new PageImpl<>(itemRequestDtoOuts, pageable, page.getTotalElements());

        return pageOut;
    }

    @Override
    public ItemRequestDtoOut getByRequestId(long userId, long requestId) {
        checkUserIdInIItemRequest(userId); // проверка существует ли user по id на исключение
        ItemRequest itemRequest = repository.getReferenceById(requestId);

        return mapper.fromItemRequestToItemRequestDtoOut(itemRequest);
    }


    private void checkUserIdInIItemRequest(long userId) {
        if (!userRepository.existsUserById(userId))
            throw new NotFoundException("IncorrectIdUserInClassItem"); // чтобы не прокручивался счетчик в бд
    }

}
