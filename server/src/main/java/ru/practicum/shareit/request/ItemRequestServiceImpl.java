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
import ru.practicum.shareit.GeneratePageableObj;
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
    GeneratePageableObj myServicePage;
    Sort sortCreatedDesc = Sort.by(Sort.Direction.DESC, "created");

    @Override
    public List<ItemRequestDtoOut> getAllByUserId(long userId) {
        checkUserId(userId);
        List<ItemRequest> itemRequests = repository.findAllByRequestor_Id(userId, sortCreatedDesc);

        List<ItemRequestDtoOut> itemRequestsList = mapper.fromListItemRequestToListItemRequestDtoOut(itemRequests);
        // заполняем запросы
        helpToAddItem(itemRequests, itemRequestsList); // добавление списка Item

        return itemRequestsList;
    }

    @Override
    @Transactional
    public ItemRequestDtoOut addItemRequest(long userId, ItemRequestDtoIn itemRequestDtoIn) {
        checkUserId(userId);
        ItemRequest itemRequest = mapper.fromItemRequestDtoInToItemRequest(itemRequestDtoIn); // начало маппинга
        itemRequest.setRequestor(userRepository.getReferenceById(userId)); // конец маппинга
        repository.save(itemRequest);
        return mapper.fromItemRequestToItemRequestDtoOut(itemRequest);
    }

    @Override
    public List<ItemRequestDtoOut> getAllOtherUsers(long userId) {
        checkUserId(userId);
        List<ItemRequest> itemRequests = repository.findAllByRequestor_IdIsNot(userId, sortCreatedDesc);
        return mapper.fromListItemRequestToListItemRequestDtoOut(itemRequests);
    }

    @Override
    public List<ItemRequestDtoOut> getAllOtherUsersPage(long userId, String from, String size) {
        checkUserId(userId);

        Pageable pageable = myServicePage.checkAndCreatePageable(from, size, sortCreatedDesc);

        Page<ItemRequest> page = repository.findAllByRequestor_IdIsNot(userId, pageable);

        // page mapping
        List<ItemRequestDtoOut> itemRequestDtoOuts = mapper.fromListItemRequestToListItemRequestDtoOut(
                page.getContent());
        helpToAddItem(page.getContent(), itemRequestDtoOuts); // добавление списка Item

        Page<ItemRequestDtoOut> pageOut = new PageImpl<>(itemRequestDtoOuts, pageable, page.getTotalElements());

        return pageOut.getContent();
    }

    @Override
    public ItemRequestDtoOut getByRequestId(long userId, long requestId) {
        checkUserId(userId);
        ItemRequest itemRequest = repository.getReferenceById(requestId);

        ItemRequestDtoOut itemRequestDtoOuts = mapper.fromItemRequestToItemRequestDtoOut(itemRequest);
        helpToAddItem(List.of(itemRequest), List.of(itemRequestDtoOuts)); // добавление списка Item

        return itemRequestDtoOuts;
    }

    private void helpToAddItem(List<ItemRequest> itemRequests, List<ItemRequestDtoOut> itemRequestDtoOuts) {
        List<Item> items = itemRepository.findAllByRequestIn(itemRequests);
        // раскладываем по ключу id Req и соотв ему список Item
        Map<Long, List<Item>> map = new HashMap<>();
        for (Item item :
                items) {
            Long id = item.getRequest().getId();
            if (!map.containsKey(id)) map.put(id, new ArrayList<>());
            map.get(id).add(item);
        }

        // добавляем список Item
        for (ItemRequestDtoOut itemRequestDtoOut :
                itemRequestDtoOuts) {
            Long id = itemRequestDtoOut.getId();
            if (!map.containsKey(id)) continue;
            itemRequestDtoOut.setItems(
                    itemMapper.fromListItemToListDtoOutForItemRequest(map.get(id)));
        }
    }

    private void checkUserId(long userId) {
        // проверка существует ли user по id на исключение
        if (!userRepository.existsUserById(userId))
            throw new NotFoundException("IncorrectIdUserInClassItem"); // чтобы не прокручивался счетчик в бд
    }

}
