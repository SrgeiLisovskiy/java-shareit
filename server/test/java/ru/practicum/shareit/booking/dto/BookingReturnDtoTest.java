package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingReturnDtoTest {
    @Autowired
    private JacksonTester<BookingReturnDto> json;

    @Test
    @DisplayName("Проверка BookingDto")
    void testBookingDto() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 9, 28, 1, 1);
        LocalDateTime end = LocalDateTime.of(2023, 9, 30, 1, 1);
        User user = User.builder()
                .id(1L)
                .email("email@email.ru")
                .name("User")
                .build();

        Item item = Item.builder()
                .id(1L)
                .requestId(1L)
                .name("Item")
                .description("itemTest")
                .available(true)
                .owner(user)
                .build();

        BookingReturnDto booking = BookingReturnDto.builder()
                .id(1L)
                .booker(user)
                .item(item)
                .start(start)
                .end(end)
                .status(Status.APPROVED)
                .build();

        JsonContent<BookingReturnDto> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("User");
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo("email@email.ru");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("Item");
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(true);


    }

}