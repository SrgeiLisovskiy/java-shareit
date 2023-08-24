package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;


    @PostMapping
    public Item createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Validated({Create.class})
    @RequestBody ItemDto itemDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Ошибка ввода данных вещи");
        }
        log.debug("Получен запрос: POST /items/{itemId}");
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.debug("Получен запрос: PATCH /items/{itemId}");
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public Item getItemByID(@PathVariable Long itemId) {
        return itemService.getItemByID(itemId);
    }

    @GetMapping
    public List<Item> getAllItem(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос: GET /items");
        return itemService.getAllItem(userId);
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam String text) {
        log.debug("Получен запрос: GET /items/search?text={text}");
        return itemService.searchItems(text.toLowerCase());
    }
}