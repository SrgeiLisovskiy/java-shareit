package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private ItemRequestDto itemRequestDto1;
    private ItemRequestDto itemRequestDto2;

    @BeforeEach
    void beforeEach() {
        itemRequestDto1 = ItemRequestDto.builder()
                .id(1L)
                .description("ItemRequestDto1Test")
                .created(LocalDateTime.now())
                .build();

        itemRequestDto2 = ItemRequestDto.builder()
                .id(2L)
                .description("ItemRequestDto2Test")
                .created(LocalDateTime.now())
                .build();
    }


    @Test
    @DisplayName("Проверка addRequest в ItemRequestController")
    void addRequest() throws Exception {
        when(itemRequestService.addRequest(anyLong(), any(ItemRequestDto.class))).thenReturn(itemRequestDto1);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto1.getDescription()), String.class));

        verify(itemRequestService, times(1)).addRequest(1L, itemRequestDto1);
    }


    @Test
    @DisplayName("Проверка getRequestsByUserId в ItemRequestController")
    void getRequestsByUserId() throws Exception {
        when(itemRequestService.getRequestsByUserId(anyLong())).thenReturn(List.of(itemRequestDto1, itemRequestDto2));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestDto1, itemRequestDto2))));

        verify(itemRequestService, times(1)).getRequestsByUserId(1L);
    }

    @Test
    @DisplayName("Проверка getRequest в ItemRequestController")
    void getRequest() throws Exception {
        when(itemRequestService.getRequest(anyLong(), anyLong())).thenReturn(itemRequestDto1);

        mvc.perform(get("/requests/{requestId}", itemRequestDto1.getId())
                        .content(mapper.writeValueAsString(itemRequestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto1.getDescription()), String.class));

        verify(itemRequestService, times(1)).getRequest(1L, 1L);

    }

    @Test
    @DisplayName("Проверка getRequests в ItemRequestController")
    void getRequests() throws Exception {
        when(itemRequestService.getRequests(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemRequestDto1, itemRequestDto2));

        mvc.perform(get("/requests/all")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestDto1, itemRequestDto2))));

        verify(itemRequestService, times(1)).getRequests(1L, 0, 10);
    }
}