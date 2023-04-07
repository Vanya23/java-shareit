package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.BookingPatternTime;
import ru.practicum.shareit.item.model.Comment;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentMapper {
    BookingPatternTime bookingPatternTime;


    public Comment fromCommentDtoInToComment(CommentDtoIn commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        return comment;
    }

    public CommentDtoOut fromCommentToCommentDtoOut(Comment comment) {
        CommentDtoOut commentDto = new CommentDtoOut(comment.getId(), comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated().format(bookingPatternTime.getFormatter())
        );
        return commentDto;
    }

    public List<CommentDtoOut> fromListCommentToCommentDtoOut(List<Comment> comments) {
        List<CommentDtoOut> commentDtoList = new ArrayList<>();
        for (Comment comment :
                comments) {
            commentDtoList.add(fromCommentToCommentDtoOut(comment));
        }
        return commentDtoList;
    }


}
