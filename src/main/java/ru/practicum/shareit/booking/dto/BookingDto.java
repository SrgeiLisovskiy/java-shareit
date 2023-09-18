package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@Builder
public class BookingDto {
    private Long id;
    @NotNull
    @FutureOrPresent(message = "Дата начала бронирования не может быть в прошлом")
    private LocalDateTime start;
    @NotNull
    @Future(message = "Дата окончания брони может бвть только в будущем")
    private LocalDateTime end;
    @NotNull
    private Long itemId;
    private Long bookerId;
    private Status status;
}
