package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingReturnDto createBooking(Long userId, BookingDto bookingDto) {
        if (userService.getUserByID(userId) == null) {
            throw new NotFoundException("Пльзователь с ID = " + userId + " не найден");
        }

        if (itemService.getItemByID(bookingDto.getItemId(), userId) == null) {
            throw new NotFoundException("Вещь с " + bookingDto.getItemId() + "  не найдена");
        }
        User user = UserMapper.toUser(userService.getUserByID(userId));
        Item item = itemRepository.findById(bookingDto.getItemId()).get();
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        if (item.getOwner().equals(user)) {
            throw new NotFoundException("Владелец с ID = " + userId + " не может забронировать свою вещь");
        }
        if (!item.isAvailable()) {
            throw new ValidationException("Вещь забронирована");
        }

        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new ValidationException("Начало бронирования не может быть позже его окончания");
        }
        if (booking.getStart().equals(booking.getEnd())) {
            throw new ValidationException("Время начала бронирования не должно" +
                    " быть ровна времени окончания бронирования");
        }
        bookingRepository.save(booking);
        log.info("Добавлено бронирование", bookingDto);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingReturnDto updateBooking(Long userId, Long bookingId, Boolean approved) {
        userService.getUserByID(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Бронирование с ID = " + bookingId + " не найдено"));
        Item item = itemRepository.findById(booking.getItem().getId()).get();
        if (item.getOwner().getId().equals(userId)) {
            if (approved) {
                if (booking.getStatus().equals(Status.APPROVED)) {
                    throw new ValidationException("Статус бронирования уже - Одобрен");
                }
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
            log.info("Бронирование подтверждено");
            return BookingMapper.toBookingDto(bookingRepository.save(booking));
        } else {
            throw new NotFoundException("Пользователь с ID = " + userId
                    + " не имеет прав на внесение изменений в бронирование");
        }

    }

    @Override
    public BookingReturnDto getBookingById(Long userId, Long bookingId) {
        userService.getUserByID(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Бронтрование с ID = " + bookingId + "  не найдено"));
        Item item = itemRepository.getById(bookingRepository.getById(bookingId).getItem().getId());
        if (booking.getBooker().getId().equals(userId) || item.getOwner().getId().equals(userId)) {
            log.info("Получена информация по id ={} бронирования", bookingId);
            return BookingMapper.toBookingDto(bookingRepository.findById(bookingId).get());
        }
        throw new NotFoundException("Пользователь с ID = " + userId + " не может внести изменения в бронирование");
    }

    @Override
    public List<BookingReturnDto> getBookingsByBookerId(Long userId, String stateText) {
        userService.getUserByID(userId);
        State state = State.returnState(stateText);
        List<Booking> bookings = bookingRepository.findAll();
        switch (state) {
            case ALL: {
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            }
            case CURRENT: {
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now());
                break;
            }
            case PAST: {
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId,
                        LocalDateTime.now());
                break;
            }
            case FUTURE: {
                bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId,
                        LocalDateTime.now());
                break;
            }
            case WAITING: {
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                break;
            }
            case REJECTED: {
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                break;
            }
        }
        log.info("Получен список бронирования по id ={} пользователя", userId);
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingReturnDto> getAllBookingsForAllItemsByOwnerId(Long userId, String stateText) {
        userService.getUserByID(userId);
        State state = State.returnState(stateText);
        List<Booking> bookings = bookingRepository.findAll();
        switch (state) {
            case ALL: {
                bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
                break;
            }
            case CURRENT: {
                bookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now());
                break;
            }
            case PAST: {
                bookings = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId,
                        LocalDateTime.now());
                break;
            }
            case FUTURE: {
                bookings = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId,
                        LocalDateTime.now());
                break;
            }
            case WAITING: {
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                break;
            }
            case REJECTED: {
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                break;
            }
        }
        log.info("Получен список всех бронирований всех товаров по ID = {} пользователя", userId);
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}
