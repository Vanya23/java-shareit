package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.ArrayList;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    boolean existsById(Long id);

    ArrayList<Comment> findAllByItem_IdOrderById(Long itemId);

}
