package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {
    private Item item;

    private User user;


    private BookingDto bookingDto;

    private Booking booking;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .email("email@email.ru")
                .name("user1")
                .build();


        item = Item.builder()
                .requestId(1L)
                .name("Item")
                .description("itemTest")
                .available(true)
                .owner(user)
                .build();

        bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2023, 9, 28, 1, 1))
                .end(LocalDateTime.of(2023, 9, 30, 1, 1))
                .build();

        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 9, 28, 1, 1))
                .end(LocalDateTime.of(2023, 9, 30, 1, 1))
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .build();

    }


    @Test
    @DisplayName("Проверка toBooking в BookingMapperTest")
    void toBooking() {
        Booking bookingTest = BookingMapper.toBooking(bookingDto);

        assertEquals(bookingDto.getId(), bookingTest.getId());
        assertEquals(bookingDto.getStart(), bookingTest.getStart());
        assertEquals(bookingDto.getEnd(), bookingTest.getEnd());
        assertEquals(bookingTest.getStatus(), Status.WAITING);

    }

    @Test
    @DisplayName("Проверка toBookingDto в BookingMapperTest")
    void toBookingDto() {
        BookingReturnDto bookingTest = BookingMapper.toBookingDto(booking);

        assertEquals(booking.getId(), bookingTest.getId());
        assertEquals(booking.getStart(), bookingTest.getStart());
        assertEquals(booking.getEnd(), bookingTest.getEnd());
        assertEquals(booking.getItem(), bookingTest.getItem());
        assertEquals(booking.getBooker(), bookingTest.getBooker());
        assertEquals(booking.getStatus(), bookingTest.getStatus());

    }

    @Test
    @DisplayName("Проверка toBookingShortDto в BookingMapperTest")
    void toBookingShortDto() {
        BookingShortDto bookingTest = BookingMapper.toBookingShortDto(booking);

        assertEquals(booking.getId(), bookingTest.getId());
        assertEquals(booking.getStart(), bookingTest.getStart());
        assertEquals(booking.getEnd(), bookingTest.getEnd());
        assertEquals(booking.getBooker().getId(), bookingTest.getBookerId());
    }
}