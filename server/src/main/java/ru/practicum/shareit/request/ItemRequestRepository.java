package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    Page<ItemRequest> findAllByRequestor_IdIsNot(long userId, Pageable pageable);

    List<ItemRequest> findAllByRequestor_Id(long userId, Sort sort);

    List<ItemRequest> findAllByRequestor_IdIsNot(long userId, Sort sortCreatedDesc);
}
