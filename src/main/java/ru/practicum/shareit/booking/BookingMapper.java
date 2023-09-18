package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

public class BookingMapper {
    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();

        if (bookingDto.getStatus() == null) {
            booking.setStatus(Status.WAITING);
        } else {
            booking.setStatus(bookingDto.getStatus());
        }
        return booking;
    }

    public static BookingReturnDto toBookingDto(Booking booking) {
        return BookingReturnDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .status(booking.getStatus())
                .build();
    }

    public static BookingShortDto toBookingShortDto(Booking booking) {
        return BookingShortDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }
}
