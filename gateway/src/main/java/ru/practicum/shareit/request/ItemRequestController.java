package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static ru.practicum.shareit.utilite.Constant.HEADER_USER_ID;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader(HEADER_USER_ID) Long userId,
                                             @Validated @RequestBody ItemRequestDto itemRequestDto) {
        log.debug("Получен запрос: POST /requests");
        return itemRequestClient.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUserId(@RequestHeader(HEADER_USER_ID) Long userId) {
        log.debug("Получен запрос: GET /requests");
        return itemRequestClient.getRequestsByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(HEADER_USER_ID) Long userId,
                                                 @PathVariable("requestId") Long requestId) {
        log.debug("Получен запрос: GET /requests/{requestId}");
        return itemRequestClient.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequests(@RequestHeader(HEADER_USER_ID) Long userId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Получен запрос: GET /requests/all");
        return itemRequestClient.getRequests(userId, from, size);
    }


}

