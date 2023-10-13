package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
	private long itemId;
	@FutureOrPresent(message = "Время начала не может быть в прошлом")
	@NotNull(message = "Время начала не может быть null")
	private LocalDateTime start;
	@Future(message = "Время завершения должно быть в будущем")
	@NotNull(message = "Время завершения не может быть null")
	private LocalDateTime end;
}
