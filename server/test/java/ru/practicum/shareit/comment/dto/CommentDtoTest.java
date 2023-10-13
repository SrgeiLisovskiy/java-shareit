package ru.practicum.shareit.comment.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    @DisplayName("Проверка CommentDto")
    void testCommentDto() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("email@email.ru")
                .name("User")
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .authorName(user.getName())
                .text("CommentDtoTest")
                .itemId(1L)
                .created(LocalDateTime.now())
                .build();

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("CommentDtoTest");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("User");
    }


}