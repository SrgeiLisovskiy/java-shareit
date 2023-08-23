package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Long userId, ItemDto itemDto);

    Item updateItem(Long userId, Long id, ItemDto itemDto);

    Item getItemByID(Long id);

    List<Item> getAllItem(Long userId);

    List<Item> searchItems(String text);
}
