package ru.practicum.shareit.item;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner(User id, Sort sort);

    boolean existsById(Long id);

    @Query(value = "select * from items " +
            " where (lower(description) LIKE lower(concat('%', ?1, '%')) or lower(full_name) LIKE lower(concat('%', ?1, '%')))" +
            "and available = true " +
            " order by id;", nativeQuery = true)
    ArrayList<Item> searchItemByText(String textForFind);

}
