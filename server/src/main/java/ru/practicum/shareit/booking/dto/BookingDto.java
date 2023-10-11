package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;


@Data
@Builder
public class BookingDto {
    private long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
}
