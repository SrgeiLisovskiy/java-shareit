package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addRequest(Long userId, ItemRequestDto itemRequestDto);

    ItemRequestDto getRequest(Long userId, Long requestId);

    List<ItemRequestDto> getRequestsByUserId(Long userId);

    List<ItemRequestDto> getRequests(Long userId, Integer from, Integer size);

    PageRequest checkPageSize(Integer from, Integer size);
}
