package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.Set;


@Data
@Builder
public class ItemDto  {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long request;
    private Long owner;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private Set<CommentDto> comments;
    private Long requestId;
}
