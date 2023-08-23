package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private Map<Long, Item> items = new HashMap<>();
    private long id;

    @Override
    public Item createItem(Long userId, ItemDto itemDto) {
        if (userService.getUserByID(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        id++;
        Item item = ItemMapper.toItem(itemDto);
        item.setId(id);
        item.setOwner(userService.getUserByID(userId));
        item.setAvailable(Boolean.valueOf(itemDto.getAvailable()));
        items.put(id, item);
        log.info("Вещь добавлена: {}", item);
        return item;
    }

    @Override
    public Item updateItem(Long userId, Long id, ItemDto itemDto) {
        if (items.get(id) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (items.get(id).getOwner().getId() != userId) {
            throw new NotFoundException("Вещь не найдена");
        }
        Item item = items.get(id);
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getAvailable() != null) {
            if (item.isAvailable() != Boolean.valueOf(itemDto.getAvailable())) {
                item.setAvailable(Boolean.valueOf(itemDto.getAvailable()));
            }
        }
        log.info("Вещь обновлена: {}", item);
        return items.get(id);
    }

    @Override
    public Item getItemByID(Long id) {
        if (items.get(id) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Получение вещи с ID = {}: ", id);
        return items.get(id);
    }

    @Override
    public List<Item> getAllItem(Long userId) {
        userService.getUserByID(userId);
        log.info("Список вещей получен");
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());

    }

    @Override
    public List<Item> searchItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        log.info("Получен список вещей по запросу {}: ", text);
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text) || item.getDescription()
                        .toLowerCase().contains(text))
                .filter(item -> item.isAvailable())
                .collect(Collectors.toList());
    }

}
