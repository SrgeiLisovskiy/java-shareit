package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemController {
    private final ItemService itemService;


    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Validated({Create.class})
    @RequestBody ItemDto itemDto) {
        log.debug("Получен запрос: POST /items/{itemId}");
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.debug("Получен запрос: PATCH /items/{itemId}");
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemByID(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable Long itemId) {
        return itemService.getItemByID(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItem(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос: GET /items");
        return itemService.getAllItem(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.debug("Получен запрос: GET /items/search?text={text}");
        return itemService.searchItems(text.toLowerCase());
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody @Validated CommentDto commentDto) {

        log.debug("Получен запрос: POST /items/{itemId}/comment");
        return itemService.addComment(userId, itemId, commentDto);
    }
}