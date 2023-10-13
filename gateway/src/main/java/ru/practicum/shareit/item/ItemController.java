package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utilite.Create;

import java.util.Collections;

import static ru.practicum.shareit.utilite.Constant.HEADER_USER_ID;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;


    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(HEADER_USER_ID) Long userId, @Validated({Create.class})
    @RequestBody ItemDto itemDto) {
        log.debug("Получен запрос: POST /items/{itemId}");
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(HEADER_USER_ID) Long userId,
                                             @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.debug("Получен запрос: PATCH /items/{itemId}");
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemByID(@RequestHeader(HEADER_USER_ID) Long userId,
                                              @PathVariable Long itemId) {
        return itemClient.getItemByID(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(HEADER_USER_ID) Long userId,
                                              @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Получен запрос: GET /items");
        return itemClient.getAllItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        log.debug("Получен запрос: GET /items/search?text={text}");
        return itemClient.searchItems(text.toLowerCase(), from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(HEADER_USER_ID) Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody @Validated CommentDto commentDto) {

        log.debug("Получен запрос: POST /items/{itemId}/comment");
        return itemClient.addComment(userId, itemId, commentDto);
    }
}