package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.transaction.Transactional;
import java.util.List;

import static ru.practicum.shareit.utilite.Constant.HEADER_USER_ID;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemController {
    private final ItemService itemService;


    @PostMapping
    public ItemDto createItem(@RequestHeader(HEADER_USER_ID) Long userId, @RequestBody ItemDto itemDto) {
        log.debug("Получен запрос: POST /items/{itemId}");
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(HEADER_USER_ID) Long userId,
                              @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.debug("Получен запрос: PATCH /items/{itemId}");
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemByID(@RequestHeader(HEADER_USER_ID) Long userId,
                               @PathVariable Long itemId) {
        return itemService.getItemByID(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(HEADER_USER_ID) Long userId,
                                     @RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Получен запрос: GET /items");
        return itemService.getAllItem(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text,
                                     @RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Получен запрос: GET /items/search?text={text}");
        return itemService.searchItems(text.toLowerCase(), from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(HEADER_USER_ID) Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody @Validated CommentDto commentDto) {

        log.debug("Получен запрос: POST /items/{itemId}/comment");
        return itemService.addComment(userId, itemId, commentDto);
    }
}