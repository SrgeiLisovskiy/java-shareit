package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class ItemRequestServiceImplTest {
    @Autowired
    private ItemRequestService itemRequestService;

    @MockBean
    private ItemRequestRepository itemRequestRepository;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private UserService userService;

    private User user;
    private UserDto userDto;
    private Item item;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .email("email@email.ru")
                .name("user1")
                .build();

        userDto = UserMapper.toUserDto(user);

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("ItemRequestTest")
                .requester(user)
                .created(LocalDateTime.of(2023, 10, 10, 1, 1))
                .build();

        itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        item = Item.builder()
                .id(1L)
                .name("Item")
                .description("itemTest")
                .owner(user)
                .available(true)
                .requestId(1L)
                .build();
    }


    @Test
    @DisplayName("Проверка addRequest в ItemRequestServiceImpl")
    void addRequest() {
        when(userService.getUserByID(anyLong())).thenReturn(userDto);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto itemRequestDtoTest = itemRequestService.addRequest(user.getId(), itemRequestDto);

        assertEquals(itemRequestDto.getId(), itemRequestDtoTest.getId());
        assertEquals(itemRequestDto.getDescription(), itemRequestDtoTest.getDescription());

        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));

    }

    @Test
    @DisplayName("Проверка getRequest в ItemRequestServiceImpl")
    void getRequest() {
        when(userService.getUserByID(anyLong())).thenReturn(userDto);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(List.of(item));

        ItemRequestDto itemRequestDtoTest = itemRequestService.getRequest(user.getId(), item.getId());

        assertEquals(itemRequestDtoTest.getId(), itemRequest.getId());
        assertEquals(itemRequestDtoTest.getDescription(), itemRequest.getDescription());
        assertEquals(itemRequestDtoTest.getItems().get(0).getId(), item.getId());
        assertEquals(itemRequestDtoTest.getItems().get(0).getRequestId(), userDto.getId());


        verify(itemRequestRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Проверка getRequestsByUserId в ItemRequestServiceImpl")
    void getRequestsByUserId() {
        when(userService.getUserByID(anyLong())).thenReturn(userDto);
        when(itemRequestRepository.findByRequesterIdOrderByCreatedAsc(anyLong())).thenReturn(List.of(itemRequest));
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(List.of(item));

        ItemRequestDto itemRequestDtoTest = itemRequestService.getRequestsByUserId(userDto.getId()).get(0);

        assertEquals(itemRequestDtoTest.getId(), itemRequest.getId());
        assertEquals(itemRequestDtoTest.getDescription(), itemRequest.getDescription());
        assertEquals(itemRequestDtoTest.getItems().get(0).getId(), item.getId());
        assertEquals(itemRequestDtoTest.getItems().get(0).getRequestId(), userDto.getId());

        verify(itemRequestRepository, times(1)).findByRequesterIdOrderByCreatedAsc(anyLong());
    }

    @Test
    @DisplayName("Проверка getRequests в ItemRequestServiceImpl")
    void getRequests() {
        when(userService.getUserByID(anyLong())).thenReturn(userDto);
        when(itemRequestRepository
                .findByRequesterIdNotOrderByCreatedAsc(anyLong(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(List.of(item));

        ItemRequestDto itemRequestDtoTest = itemRequestService.getRequests(userDto.getId(), 5, 10).get(0);

        assertEquals(itemRequestDtoTest.getId(), itemRequest.getId());
        assertEquals(itemRequestDtoTest.getDescription(), itemRequest.getDescription());
        assertEquals(itemRequestDtoTest.getItems().get(0).getId(), item.getId());
        assertEquals(itemRequestDtoTest.getItems().get(0).getRequestId(), userDto.getId());

        verify(itemRequestRepository, times(1))
                .findByRequesterIdNotOrderByCreatedAsc(anyLong(), any(PageRequest.class));
    }

    @Test
    void getRequestErrorRequestId() {
        when(userService.getUserByID(anyLong())).thenReturn(userDto);

        assertThrows(NotFoundException.class, () -> itemRequestService.getRequest(1L, 1L));

    }
}