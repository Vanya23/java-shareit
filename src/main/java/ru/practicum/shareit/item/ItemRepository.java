package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner(User id, Sort sort);

    List<Item> findAllByRequestIn(List<ItemRequest> req);

    List<Item> findAllByOwner_Id(long userId, Sort sort);

    Page<Item> findAllByOwner_Id(long userId, Pageable pageable);

    boolean existsById(Long id);

    @Query(value = "select * from items " +
            " where (lower(description) LIKE lower(concat('%', ?1, '%')) or lower(full_name) LIKE lower(concat('%', ?1, '%')))" +
            "and available = true " +
            " order by id;", nativeQuery = true)
    List<Item> searchItemByText(String textForFind);

    @Query(value = "select * from items " +
            " where (lower(description) LIKE lower(concat('%', ?1, '%')) or lower(full_name) LIKE lower(concat('%', ?1, '%')))" +
            "and available = true " +
            " order by id;", nativeQuery = true)
    Page<Item> searchItemByTextPage(String textForFind, Pageable pageable);


}
