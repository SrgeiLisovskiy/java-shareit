package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ItemServiceImplTest {
    @Autowired
    private ItemService itemService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private CommentRepository commentRepository;

    private User user;
    private Item item;
    private ItemRequest itemRequest;
    private Comment comment;
    private Booking nextBooking;
    private Booking lastBooking;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("User")
                .email("user@mail.ru")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("ItemRequestTest")
                .requester(user)
                .created(LocalDateTime.now())
                .build();

        item = Item.builder()
                .id(1L)
                .name("Item")
                .description("itemTest")
                .owner(user)
                .available(true)
                .requestId(1L)
                .build();

        comment = Comment.builder()
                .id(1L)
                .author(user)
                .created(LocalDateTime.now())
                .text("comment test")
                .build();

        nextBooking = Booking.builder()
                .id(1L)
                .booker(user)
                .item(item)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .status(Status.APPROVED)
                .build();

        lastBooking = Booking.builder()
                .id(2L)
                .booker(user)
                .item(item)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .status(Status.APPROVED)
                .build();

    }

    @Test
    @DisplayName("Проверка createItem в ItemServiceImpl")
    void createItem() {
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user));

        ItemDto itemDto = itemService.createItem(anyLong(), ItemMapper.toItemDto(item));
        assertEquals(1, itemDto.getId());
        assertEquals("Item", itemDto.getName());
        assertEquals("itemTest", itemDto.getDescription());
        assertEquals(user.getId(), itemDto.getOwner());
        assertEquals(1, itemDto.getRequestId());
        assertEquals(true, itemDto.getAvailable());

        verify(itemRepository, times(1)).save(any(Item.class));
    }


    @Test
    @DisplayName("Проверка updateItem в ItemServiceImpl")
    void updateItem() {
        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user));
        when(itemRepository.getById(anyLong())).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("update")
                .description("updateTest")
                .available(false)
                .build();
        ItemDto itemDtoTest = itemService.updateItem(1L, 1L, itemDto);

        assertEquals(1, itemDtoTest.getId());
        assertEquals("update", itemDtoTest.getName());
        assertEquals("updateTest", itemDtoTest.getDescription());
        assertEquals(false, itemDtoTest.getAvailable());

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItemErrorId() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        assertThrows(NotFoundException.class, () -> itemService.updateItem(1L, 1L, ItemMapper.toItemDto(item)));
    }

    @Test
    void updateItemErrorUserId() {
        when(userService.getUserByID(anyLong())).thenReturn(null);
        when(itemRepository.getById(anyLong())).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        assertThrows(NotFoundException.class, () -> itemService.updateItem(1L, 1L, ItemMapper.toItemDto(item)));
    }

    @Test
    @DisplayName("Проверка getItemByID в ItemServiceImpl")
    void getItemByID() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(
                item.getId(), Status.APPROVED, LocalDateTime.now())).thenReturn(Optional.ofNullable(lastBooking));
        when(bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
                item.getId(), Status.APPROVED, LocalDateTime.now())).thenReturn(Optional.ofNullable(nextBooking));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(Set.of(comment));

        ItemDto itemDtoTest = itemService.getItemByID(item.getId(), user.getId());

        assertEquals(item.getId(), itemDtoTest.getId());
        assertEquals(item.getName(), itemDtoTest.getName());
        assertEquals(item.getDescription(), itemDtoTest.getDescription());
        assertEquals(item.isAvailable(), itemDtoTest.getAvailable());

        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void getItemByIdErrorItemId() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(
                item.getId(), Status.APPROVED, LocalDateTime.now())).thenReturn(Optional.ofNullable(lastBooking));
        when(bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
                item.getId(), Status.APPROVED, LocalDateTime.now())).thenReturn(Optional.ofNullable(nextBooking));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(Set.of(comment));

        assertThrows(NotFoundException.class, () -> itemService.getItemByID(1L, 1L));
    }


    @Test
    @DisplayName("Проверка getAllItem в ItemServiceImpl")
    void getAllItem() {
        when(itemRepository.findByOwnerId(anyLong(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(item)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

        List<ItemDto> itemDtoTest = itemService.getAllItem(user.getId(), 1, 1);
        ItemDto itemDto = itemDtoTest.get(0);

        assertEquals(1, itemDtoTest.size());
        assertEquals(1L, itemDto.getId());
        assertEquals("Item", itemDto.getName());
        assertEquals("itemTest", itemDto.getDescription());
        assertEquals(user.getId(), itemDto.getOwner());
        assertEquals(1, itemDto.getRequestId());
        assertEquals(true, itemDto.getAvailable());

        verify(itemRepository, times(1)).findByOwnerId(anyLong(), any(PageRequest.class));
    }

    @Test
    @DisplayName("Проверка searchItems в ItemServiceImpl")
    void searchItems() {
        when(itemRepository.search(anyString(), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(item)));

        List<ItemDto> itemDtoTest = itemService.searchItems(item.getDescription(), 1, 1);
        ItemDto itemDto = itemDtoTest.get(0);

        assertEquals(1, itemDtoTest.size());
        assertEquals(1L, itemDto.getId());
        assertEquals("Item", itemDto.getName());
        assertEquals("itemTest", itemDto.getDescription());
        assertEquals(user.getId(), itemDto.getOwner());
        assertEquals(1, itemDto.getRequestId());
        assertEquals(true, itemDto.getAvailable());

        verify(itemRepository, times(1)).search(anyString(), any(PageRequest.class));
    }

    @Test
    @DisplayName("Проверка searchItems с пустым текстом поиска в ItemServiceImpl")
    void searchItemsEmptyText() {
        List<ItemDto> itemDtoTest = itemService.searchItems("", 5, 10);

        assertTrue(itemDtoTest.isEmpty());

        verify(itemRepository, times(0)).search(anyString(), any(PageRequest.class));
    }

    @Test
    @DisplayName("Проверка addComment в ItemServiceImpl")
    void addComment() {
        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndEndBefore(
                anyLong(), anyLong(), any(Status.class), any(LocalDateTime.class)))
                .thenReturn(Optional.ofNullable(nextBooking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto commentDto = itemService.addComment(user.getId(), item.getId(), CommentMapper.toCommentDto(comment));
        assertEquals(comment.getId(), commentDto.getId());
        assertEquals(comment.getText(), commentDto.getText());
        assertEquals(comment.getItemId(), commentDto.getItemId());
        assertEquals(comment.getAuthor(), user);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void addCommentUserNotBookingItem() {
        when(userService.getUserByID(anyLong())).thenReturn(UserMapper.toUserDto(user));
        when(bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndEndBefore(
                anyLong(), anyLong(), any(Status.class), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        assertThrows(NotFoundException.class, () ->
                itemService.addComment(user.getId(), item.getId(), CommentMapper.toCommentDto(comment)));
    }
}