package ru.practicum.shareit.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentMapperTest {
    User user = User.builder()
            .id(1L)
            .email("email@email.ru")
            .name("user1")
            .build();
    Comment comment = Comment.builder()
            .id(1L)
            .itemId(1L)
            .author(user)
            .text("CommentTest")
            .created(LocalDateTime.now())
            .build();

    CommentDto commentDto = CommentDto.builder()
            .id(1L)
            .authorName(user.getName())
            .text("CommentDtoTest")
            .itemId(1L)
            .created(LocalDateTime.now())
            .build();

    @Test
    @DisplayName("Проверка toCommentDto в CommentMapper")
    void toCommentDto() {
        CommentDto commentTest = CommentMapper.toCommentDto(comment);

        assertEquals(comment.getId(), commentTest.getId());
        assertEquals(comment.getItemId(), commentTest.getItemId());
        assertEquals(comment.getAuthor().getName(), commentTest.getAuthorName());
        assertEquals(comment.getText(), commentTest.getText());
        assertEquals(comment.getCreated(), commentTest.getCreated());
    }

    @Test
    @DisplayName("Проверка toComment в CommentMapper")
    void toComment() {
        Comment commentTest = CommentMapper.toComment(commentDto, user, 1L);

        assertEquals(commentDto.getId(), commentTest.getId());
        assertEquals(commentDto.getAuthorName(), commentTest.getAuthor().getName());
        assertEquals(commentDto.getText(),commentTest.getText());
        assertEquals(1L, commentTest.getItemId());
    }
}