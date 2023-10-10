package ru.practicum.shareit.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {
    User user = User.builder()
            .id(1L)
            .email("email@email.ru")
            .name("user1")
            .build();
    Item item = Item.builder()
            .id(1L)
            .name("Item")
            .description("itemTest")
            .available(true)
            .owner(user)
            .requestId(1L)
            .build();

    ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("ItemDto")
            .description("itemDtoTest")
            .available(true)
            .owner(user.getId())
            .requestId(1L)
            .build();

    @Test
    @DisplayName("Проверка toItemDto в ItemMapper")
    void toItemDto() {
        ItemDto itemDtoResult = ItemMapper.toItemDto(item);

        assertEquals(item.getId(), itemDtoResult.getId());
        assertEquals(item.getName(), itemDtoResult.getName());
        assertEquals(item.getDescription(), itemDtoResult.getDescription());
        assertEquals(item.getRequestId(), itemDtoResult.getRequestId());
        assertEquals(item.isAvailable(), itemDtoResult.getAvailable());
        assertEquals(item.getOwner().getId(), itemDtoResult.getOwner());

    }

    @Test
    @DisplayName("Проверка toItem в ItemMapper")
    void toItem() {
        Item itemResult = ItemMapper.toItem(itemDto, user);

        assertEquals(itemDto.getId(), itemResult.getId());
        assertEquals(itemDto.getName(), itemResult.getName());
        assertEquals(itemDto.getDescription(), itemResult.getDescription());
        assertEquals(itemDto.getRequestId(), itemResult.getRequestId());
        assertEquals(itemDto.getAvailable(), itemResult.isAvailable());
        assertEquals(itemDto.getOwner(), itemResult.getOwner().getId());

    }
}