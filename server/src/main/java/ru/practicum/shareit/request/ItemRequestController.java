package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

import static ru.practicum.shareit.utilite.Constant.HEADER_USER_ID;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader(HEADER_USER_ID) Long userId,
                                     @RequestBody ItemRequestDto itemRequestDto) {
        log.debug("Получен запрос: POST /requests");
        return itemRequestService.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getRequestsByUserId(@RequestHeader(HEADER_USER_ID) Long userId) {
        log.debug("Получен запрос: GET /requests");
        return itemRequestService.getRequestsByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader(HEADER_USER_ID) Long userId,
                                     @PathVariable("requestId") Long requestId) {
        log.debug("Получен запрос: GET /requests/{requestId}");
        return itemRequestService.getRequest(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getRequests(@RequestHeader(HEADER_USER_ID) Long userId,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Получен запрос: GET /requests/all");
        return itemRequestService.getRequests(userId, from, size);
    }


}

