package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;
    private Item item;

    private User user;


    private BookingDto bookingDto;

    private BookingReturnDto nextBookingDto;

    private BookingReturnDto lastBookingDto;

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

        nextBookingDto = BookingReturnDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 9, 28, 1, 1))
                .end(LocalDateTime.of(2023, 9, 30, 1, 1))
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .build();

        lastBookingDto = BookingReturnDto.builder()
                .id(2L)
                .start(LocalDateTime.of(2023, 9, 28, 1, 1))
                .end(LocalDateTime.of(2023, 9, 30, 1, 1))
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .build();
    }


    @Test
    @DisplayName("Проверка addBooking в BookingController")
    void addBooking() throws Exception {
        when(bookingService.createBooking(anyLong(), any(BookingDto.class))).thenReturn(nextBookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(nextBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(nextBookingDto.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$.booker.id", is(nextBookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(nextBookingDto.getItem().getId()), Long.class));

        verify(bookingService, times(1)).createBooking(1L, bookingDto);

    }

    @Test
    @DisplayName("Проверка updateBooking в BookingController")
    void updateBooking() throws Exception {
        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(nextBookingDto);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(nextBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(nextBookingDto.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$.booker.id", is(nextBookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(nextBookingDto.getItem().getId()), Long.class));

        verify(bookingService, times(1)).updateBooking(1L, 1L, true);
    }

    @Test
    @DisplayName("Проверка getBooking в BookingController")
    void getBooking() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(nextBookingDto);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(nextBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(nextBookingDto.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$.booker.id", is(nextBookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(nextBookingDto.getItem().getId()), Long.class));

        verify(bookingService, times(1)).getBookingById(1L, 1L);
    }

    @Test
    @DisplayName("Проверка getBookingsByUserId в BookingController")
    void getBookingsByUserId() throws Exception {
        when(bookingService.getBookingsByBookerId(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(nextBookingDto, lastBookingDto));

        mvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(nextBookingDto, lastBookingDto))));

        verify(bookingService, times(1)).getBookingsByBookerId(1L, "ALL", 0, 10);
    }

    @Test
    @DisplayName("Проверка getAllBookingsForAllItemsByOwnerId в BookingController")
    void getAllBookingsForAllItemsByOwnerId() throws Exception {
        when(bookingService.getAllBookingsForAllItemsByOwnerId(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(nextBookingDto, lastBookingDto));

        mvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(nextBookingDto, lastBookingDto))));

        verify(bookingService, times(1)).getAllBookingsForAllItemsByOwnerId(1L, "ALL", 0, 10);
    }
}