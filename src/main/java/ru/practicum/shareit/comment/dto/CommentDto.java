package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
@Data
@Builder
public class CommentDto {
    private Long id;
    @NotBlank
    @NotEmpty(message = "Комментарий не может быть пустым")
    private String text;
    private String authorName;
    private Long itemId;
    private LocalDateTime created;
}
