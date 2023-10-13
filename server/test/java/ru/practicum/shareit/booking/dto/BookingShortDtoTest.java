package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingShortDtoTest {

    @Autowired
    private JacksonTester<BookingShortDto> json;

    @Test
    @DisplayName("Проверка BookingShortDto")
    void testBookingShortDto() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("email@email.ru")
                .name("User")
                .build();

        LocalDateTime start = LocalDateTime.of(2023, 9, 28, 1, 1);
        LocalDateTime end = LocalDateTime.of(2023, 9, 30, 1, 1);

        BookingShortDto bookingShortDto = BookingShortDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .bookerId(user.getId())
                .build();

        JsonContent<BookingShortDto> result = json.write(bookingShortDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
    }


}