package ru.practicum.shareit.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .itemId(comment.getItemId())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(CommentDto commentDto, User user, Long itemId) {
        return Comment.builder()
                .id(commentDto.getId())
                .itemId(itemId)
                .author(user)
                .text(commentDto.getText())
                .created(LocalDateTime.now())
                .build();
    }
}
