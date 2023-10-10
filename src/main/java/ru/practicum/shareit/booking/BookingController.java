package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;

import java.util.List;

import static ru.practicum.shareit.utilite.Constant.HEADER_USER_ID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingReturnDto addBooking(@RequestHeader(HEADER_USER_ID) Long userId,
                                       @Validated @RequestBody BookingDto bookingDto) {
        log.debug("Получен запрос на добавление бронирования");
        return bookingService.createBooking(userId, bookingDto);

    }

    @PatchMapping("/{bookingId}")
    public BookingReturnDto updateBooking(@RequestHeader(HEADER_USER_ID) Long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam boolean approved) {
        log.info("Получен запрос на подтверждение бронирования");
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingReturnDto getBooking(@RequestHeader(HEADER_USER_ID) Long userId,
                                       @PathVariable Long bookingId) {
        log.info("Получен запрос на поиск информации по id бронирования");
        return bookingService.getBookingById(userId, bookingId);

    }

    @GetMapping
    public List<BookingReturnDto> getBookingsByUserId(@RequestParam(defaultValue = "ALL") String state,
                                                      @RequestHeader(HEADER_USER_ID) Long userId,
                                                      @RequestParam(defaultValue = "0") Integer from,
                                                      @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск бронирования по id пользователя");
        return bookingService.getBookingsByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingReturnDto> getAllBookingsForAllItemsByOwnerId(@RequestHeader(HEADER_USER_ID) Long userId,
                                                                     @RequestParam(defaultValue = "ALL") String state,
                                                                     @RequestParam(defaultValue = "0") Integer from,
                                                                     @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск всех бронирований всех товаров по ID пользователя");
        return bookingService.getAllBookingsForAllItemsByOwnerId(userId, state,from, size);
    }

}
