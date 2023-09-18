package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long id, ItemDto itemDto);

    ItemDto getItemByID(Long itemId, Long userId);

    List<ItemDto> getAllItem(Long userId);

    List<ItemDto> searchItems(String text);
    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);
}
