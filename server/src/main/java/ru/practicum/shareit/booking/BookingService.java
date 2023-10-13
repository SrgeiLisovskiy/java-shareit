package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;

import java.util.List;

interface BookingService {
    BookingReturnDto createBooking(Long id, BookingDto bookingDto);

    BookingReturnDto updateBooking(Long userId, Long bookingId, Boolean approved);

    BookingReturnDto getBookingById(Long userId, Long bookingId);

    List<BookingReturnDto> getBookingsByBookerId(Long userId, String stateText, Integer from, Integer size);

    List<BookingReturnDto> getAllBookingsForAllItemsByOwnerId(Long userId, String stateText, Integer from, Integer size);

}
