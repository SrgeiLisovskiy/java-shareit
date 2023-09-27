package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserService userService;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto addRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = UserMapper.toUser(userService.getUserByID(userId));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(user, itemRequestDto);
        itemRequestRepository.save(itemRequest);
        log.info("Сохранен запрос: {}", itemRequestDto);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto getRequest(Long userId, Long requestId) {
        userService.getUserByID(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с Id = " + requestId + "не найден"));
        log.info("Получен запрос : {}", itemRequest);
        return addItemsToRequest(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getRequestsByUserId(Long requesterId) {
        userService.getUserByID(requesterId);
        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdOrderByCreatedAsc(requesterId);
        log.info("Получен список запросов по ID ={} запрашивающего ", requesterId);
        return requests.stream().map(this::addItemsToRequest).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getRequests(Long requesterId, Integer from, Integer size) {
        userService.getUserByID(requesterId);
        Page<ItemRequest> itemRequests = itemRequestRepository
                .findByRequesterIdNotOrderByCreatedAsc(requesterId, checkPageSize(from, size));
        log.info("Получен список запросов по ID ={} запрашивающего с параметрами : страницы = {}, размер ={} ",
                requesterId, from, size);
        return itemRequests.stream().map(this::addItemsToRequest).collect(Collectors.toList());
    }

    private ItemRequestDto addItemsToRequest(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        List<ItemDto> items = itemRepository.findAllByRequestId(itemRequest.getId()).stream().map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        itemRequestDto.setItems(items);
        return itemRequestDto;
    }

    public PageRequest checkPageSize(Integer from, Integer size) {
        if (from == 0 && size == 0) {
            throw new ValidationException("размер и номер страницы не может быть равен нулю ");
        }
        if (size < 0) {
            throw new ValidationException("размер не может быть меньше чем 0");
        }

        if (from < 0) {
            throw new ValidationException("страница не может быть меньше чем 0");
        }
        return PageRequest.of(from / size, size);
    }
}
