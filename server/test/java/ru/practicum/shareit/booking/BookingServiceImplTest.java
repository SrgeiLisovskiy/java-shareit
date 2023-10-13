package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookingServiceImplTest {
    @Autowired
    private BookingService bookingService;
    @MockBean
    private BookingRepository bookingRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ItemService itemService;
    @MockBean
    private ItemRepository itemRepository;

    private User user;
    private User user2;
    private Item item;
    private ItemDto itemDto;
    private Booking nextBooking;
    private Booking lastBooking;
    private BookingDto bookingDto;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .email("email@email.ru")
                .name("user1")
                .build();
        user2 = User.builder()
                .id(2L)
                .email("email@email.ru")
                .name("user1")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Item")
                .description("itemTest")
                .owner(user)
                .available(true)
                .requestId(1L)
                .build();
        itemDto = ItemMapper.toItemDto(item);

        nextBooking = Booking.builder()
                .id(1L)
                .booker(user)
                .item(item)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.APPROVED)
                .build();

        lastBooking = Booking.builder()
                .id(2L)
                .booker(user)
                .item(item)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING)
                .build();

        bookingDto = BookingDto.builder()
                .id(2L)
                .itemId(item.getId())
                .bookerId(user2.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();
        ;

    }

    @Test
    @DisplayName("Проверка createBooking в BookingServiceImpl")
    void createBooking() {
        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user2));
        when(itemService.getItemByID(anyLong(), anyLong())).thenReturn(itemDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(nextBooking);


        BookingReturnDto bookingDtoTest = bookingService.createBooking(anyLong(), bookingDto);

        assertEquals(bookingDtoTest.getItem().getOwner().getId(), itemDto.getOwner());
        assertEquals(bookingDtoTest.getStatus(), Status.WAITING);
        assertEquals(bookingDtoTest.getBooker().getId(), bookingDto.getBookerId());
        assertEquals(bookingDtoTest.getItem().getId(), bookingDto.getItemId());

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void addBookingItemBookedError() {
        item.setAvailable(false);

        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user2));
        when(itemService.getItemByID(anyLong(), anyLong())).thenReturn(itemDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(nextBooking);

        assertThrows(ValidationException.class, () -> bookingService.createBooking(anyLong(), bookingDto));
    }

    @Test
    void createBookingErrorStart() {
        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user2));
        when(itemService.getItemByID(anyLong(), anyLong())).thenReturn(itemDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(nextBooking);

        bookingDto.setStart(LocalDateTime.of(2023, 10, 11, 1, 1));
        assertThrows(ValidationException.class, () -> bookingService.createBooking(anyLong(), bookingDto));

        bookingDto.setEnd(LocalDateTime.of(2023, 10, 11, 1, 1));
        assertThrows(ValidationException.class, () -> bookingService.createBooking(anyLong(), bookingDto));


    }

    @Test
    void createBookingErrorEnd() {
        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user2));
        when(itemService.getItemByID(anyLong(), anyLong())).thenReturn(itemDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(nextBooking);

        bookingDto.setEnd(LocalDateTime.of(2020, 10, 11, 1, 1));
        assertThrows(ValidationException.class, () -> bookingService.createBooking(anyLong(), bookingDto));
    }

    @Test
    void createBookingErrorItemById() {
        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user2));

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(anyLong(), bookingDto));
    }

    @Test
    void createBookingErrorUserByIdEqualsOwnerId() {
        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user2));
        when(itemService.getItemByID(anyLong(), anyLong())).thenReturn(itemDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(nextBooking);
        item.setOwner(user2);

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(anyLong(), bookingDto));
    }

    @Test
    void createBookingErrorUserById() {
        when(itemService.getItemByID(anyLong(), anyLong())).thenReturn(itemDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(nextBooking);

        bookingDto.setEnd(LocalDateTime.of(2020, 10, 11, 1, 1));
        assertThrows(NotFoundException.class, () -> bookingService.createBooking(anyLong(), bookingDto));
    }

    @Test
    @DisplayName("Проверка updateBooking в BookingServiceImpl")
    void updateBooking() {
        BookingReturnDto bookingDtoTest;
        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user2));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(lastBooking));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(lastBooking);

        bookingDtoTest = bookingService.updateBooking(user.getId(), lastBooking.getId(), true);
        assertEquals(bookingDtoTest.getStatus(), Status.APPROVED);

        bookingDtoTest = bookingService.updateBooking(user.getId(), lastBooking.getId(), false);
        assertEquals(bookingDtoTest.getStatus(), Status.REJECTED);

        verify(bookingRepository, times(2)).save(any(Booking.class));
    }

    @Test
    void approveBookingErrorStatus() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(nextBooking));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(nextBooking);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(ValidationException.class, () -> bookingService.updateBooking(user.getId(), nextBooking.getId(), true));

    }

    @Test
    void approveBookingErrorUser() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(lastBooking);

        assertThrows(NotFoundException.class, () -> bookingService.updateBooking(user2.getId(), lastBooking.getId(), true));
    }


    @Test
    @DisplayName("Проверка getBookingById в BookingServiceImpl")
    void getBookingById() {
        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(lastBooking));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.getById(anyLong())).thenReturn(lastBooking);

        BookingReturnDto bookingDtoTest = bookingService.getBookingById(user.getId(), lastBooking.getId());

        assertEquals(bookingDtoTest.getItem(), item);
        assertEquals(bookingDtoTest.getStatus(), lastBooking.getStatus());
        assertEquals(bookingDtoTest.getBooker(), user);
    }

    @Test
    void getBookingErrorById() {
        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.getById(anyLong())).thenReturn(lastBooking);


        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1L, 5L));

    }

    @Test
    @DisplayName("Проверка getBookingsByBookerId в BookingServiceImpl")
    void getBookingsByBookerId() {
        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user));

        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(lastBooking)));

        String state = "ALL";

        List<BookingReturnDto> bookingDtoTest = bookingService
                .getBookingsByBookerId(user.getId(), state, 5, 10);

        assertEquals(bookingDtoTest.get(0).getId(), lastBooking.getId());
        assertEquals(bookingDtoTest.get(0).getStatus(), lastBooking.getStatus());
        assertEquals(bookingDtoTest.get(0).getBooker(), user);

        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(lastBooking)));
        state = "CURRENT";

        bookingDtoTest = bookingService.getBookingsByBookerId(user.getId(), state, 5, 10);

        assertEquals(bookingDtoTest.get(0).getId(), lastBooking.getId());
        assertEquals(bookingDtoTest.get(0).getStatus(), lastBooking.getStatus());
        assertEquals(bookingDtoTest.get(0).getBooker(), user);

        when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(
                anyLong(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(lastBooking)));
        state = "PAST";

        bookingDtoTest = bookingService.getBookingsByBookerId(user.getId(), state, 5, 10);

        assertEquals(bookingDtoTest.get(0).getId(), lastBooking.getId());
        assertEquals(bookingDtoTest.get(0).getStatus(), lastBooking.getStatus());
        assertEquals(bookingDtoTest.get(0).getBooker(), user);

        when(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(
                anyLong(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(lastBooking)));
        state = "FUTURE";

        bookingDtoTest = bookingService.getBookingsByBookerId(user.getId(), state, 5, 10);

        assertEquals(bookingDtoTest.get(0).getId(), lastBooking.getId());
        assertEquals(bookingDtoTest.get(0).getStatus(), lastBooking.getStatus());
        assertEquals(bookingDtoTest.get(0).getBooker(), user);

        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                anyLong(), any(Status.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(lastBooking)));
        state = "WAITING";

        bookingDtoTest = bookingService.getBookingsByBookerId(user.getId(), state, 5, 10);

        assertEquals(bookingDtoTest.get(0).getId(), lastBooking.getId());
        assertEquals(bookingDtoTest.get(0).getStatus(), lastBooking.getStatus());
        assertEquals(bookingDtoTest.get(0).getBooker(), user);

        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                anyLong(), any(Status.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(lastBooking)));
        state = "REJECTED";

        bookingDtoTest = bookingService.getBookingsByBookerId(user.getId(), state, 5, 10);

        assertEquals(bookingDtoTest.get(0).getId(), lastBooking.getId());
        assertEquals(bookingDtoTest.get(0).getStatus(), lastBooking.getStatus());
        assertEquals(bookingDtoTest.get(0).getBooker(), user);
    }

    @Test
    @DisplayName("Проверка getAllBookingsForAllItemsByOwnerId в BookingServiceImpl")
    void getAllBookingsForAllItemsByOwnerId() {
        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user));
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(anyLong(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(lastBooking)));

        String state = "ALL";

        List<BookingReturnDto> bookingDtoTest = bookingService
                .getAllBookingsForAllItemsByOwnerId(user.getId(), state, 5, 10);

        assertEquals(bookingDtoTest.get(0).getId(), lastBooking.getId());
        assertEquals(bookingDtoTest.get(0).getStatus(), lastBooking.getStatus());
        assertEquals(bookingDtoTest.get(0).getBooker(), user);

        when(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(lastBooking)));
        state = "CURRENT";

        bookingDtoTest = bookingService.getAllBookingsForAllItemsByOwnerId(user.getId(), state, 5, 10);

        assertEquals(bookingDtoTest.get(0).getId(), lastBooking.getId());
        assertEquals(bookingDtoTest.get(0).getStatus(), lastBooking.getStatus());
        assertEquals(bookingDtoTest.get(0).getBooker(), user);

        when(bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(
                anyLong(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(lastBooking)));
        state = "PAST";

        bookingDtoTest = bookingService.getAllBookingsForAllItemsByOwnerId(user.getId(), state, 5, 10);

        assertEquals(bookingDtoTest.get(0).getId(), lastBooking.getId());
        assertEquals(bookingDtoTest.get(0).getStatus(), lastBooking.getStatus());
        assertEquals(bookingDtoTest.get(0).getBooker(), user);

        when(bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(
                anyLong(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(lastBooking)));
        state = "FUTURE";

        bookingDtoTest = bookingService.getAllBookingsForAllItemsByOwnerId(user.getId(), state, 5, 10);

        assertEquals(bookingDtoTest.get(0).getId(), lastBooking.getId());
        assertEquals(bookingDtoTest.get(0).getStatus(), lastBooking.getStatus());
        assertEquals(bookingDtoTest.get(0).getBooker(), user);

        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                anyLong(), any(Status.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(lastBooking)));
        state = "WAITING";

        bookingDtoTest = bookingService.getAllBookingsForAllItemsByOwnerId(user.getId(), state, 5, 10);

        assertEquals(bookingDtoTest.get(0).getId(), lastBooking.getId());
        assertEquals(bookingDtoTest.get(0).getStatus(), lastBooking.getStatus());
        assertEquals(bookingDtoTest.get(0).getBooker(), user);

        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                anyLong(), any(Status.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(lastBooking)));
        state = "REJECTED";

        bookingDtoTest = bookingService.getAllBookingsForAllItemsByOwnerId(user.getId(), state, 5, 10);

        assertEquals(bookingDtoTest.get(0).getId(), lastBooking.getId());
        assertEquals(bookingDtoTest.get(0).getStatus(), lastBooking.getStatus());
        assertEquals(bookingDtoTest.get(0).getBooker(), user);
    }
}