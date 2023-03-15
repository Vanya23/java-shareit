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

    public CommentDto fromCommentToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto(comment.getId(), comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated().format(bookingPatternTime.getFormatter())
        );
        return commentDto;
    }

    public List<CommentDto> fromListCommentToCommentDto(List<Comment> comments) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment :
                comments) {
            commentDtoList.add(fromCommentToCommentDto(comment));
        }
        return commentDtoList;
    }

    public Comment fromCommentDtoToComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        return comment;
    }


}
