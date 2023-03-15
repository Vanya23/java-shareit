package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;

public interface ItemRepository extends JpaRepository<Item, Long> {
    //    List<Item> findAllByOwner(User id);
    ArrayList<Item> findAllByOwnerOrderById(User id);

    //    ArrayList<Item> findAllByOwnerOrderById(User id);
    boolean existsById(Long id);

}
