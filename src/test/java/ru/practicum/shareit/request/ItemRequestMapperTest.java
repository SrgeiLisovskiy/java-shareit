package ru.practicum.shareit.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {
    User user = User.builder()
            .id(1L)
            .email("email@email.ru")
            .name("user1")
            .build();

    ItemRequest itemRequest =ItemRequest.builder()
            .id(1L)
            .requester(user)
            .description("ItemRequestTest")
            .created(LocalDateTime.now())
            .build();

    ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .id(1L)
            .description("ItemRequestDtoTest")
            .created(LocalDateTime.now())
            .items(new ArrayList<>())
            .build();

    @Test
    @DisplayName("Проверка toItemRequest в ItemRequestMapper")
    void toItemRequest() {
        ItemRequest itemRequestTest = ItemRequestMapper.toItemRequest(user,itemRequestDto);

        assertEquals(itemRequestDto.getId(),itemRequestTest.getId());
        assertEquals(itemRequestDto.getDescription(),itemRequestTest.getDescription());
    }

    @Test
    @DisplayName("Проверка toItemRequestDto в ItemRequestMapper")
    void toItemRequestDto() {
        ItemRequestDto itemRequestDtoTest = ItemRequestMapper.toItemRequestDto(itemRequest);

        assertEquals(itemRequest.getId(), itemRequestDtoTest.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDtoTest.getDescription());
    }
}