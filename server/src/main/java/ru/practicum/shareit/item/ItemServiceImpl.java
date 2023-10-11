package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.utilite.CheckValidationService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CheckValidationService checkValidation;

    @Override
    @Transactional
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User user = UserMapper.toUser(userService.getUserByID(userId));
        Item item = ItemMapper.toItem(itemDto, user);
        log.info("Создана вещь", itemDto);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, Long id, ItemDto itemDto) {
        if (itemRepository.getById(id) == null) {
            throw new NotFoundException("Вещь с " + id + "  не найдена");
        }
        if (userService.getUserByID(userId) == null) {
            throw new NotFoundException("Пользователь с " + id + "  не найдена");
        }
        Item item = itemRepository.getById(id);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        log.info("Вещь с ID = {} обновлен", id);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemByID(Long itemId, Long userId) {
        userService.getUserByID(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Вещь с " + itemId + "  не найдена"));
        ItemDto itemDto = ItemMapper.toItemDto(item);
        if (item.getOwner().getId() == userId) {
            Optional<Booking> lastBooking = bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(
                    itemId, Status.APPROVED, LocalDateTime.now());
            Optional<Booking> nextBooking = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
                    itemId, Status.APPROVED, LocalDateTime.now());
            if (lastBooking.isPresent()) {
                itemDto.setLastBooking(BookingMapper.toBookingShortDto(lastBooking.get()));
            } else
                itemDto.setLastBooking(null);
            if (nextBooking.isPresent()) {
                itemDto.setNextBooking(BookingMapper.toBookingShortDto(nextBooking.get()));
            } else
                itemDto.setNextBooking(null);
        }
        itemDto.setComments(commentRepository.findAllByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toSet()));
        log.info("Получена вещь по ID = {}", itemId);
        return itemDto;
    }

    @Override
    public List<ItemDto> getAllItem(Long userId, Integer from, Integer size) {
        log.info("Получен список всех вещей пользователя с ID = {}", userId);
        List<ItemDto> itemDtoList = itemRepository.findByOwnerId(userId, PageRequest.of(from, size)).stream()
                .map(item -> getItemByID(item.getId(), userId))
                .sorted(Comparator.comparing(ItemDto::getId)).collect(Collectors.toList());
        return itemDtoList;
    }


    @Override
    public List<ItemDto> searchItems(String text, Integer from, Integer size) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        PageRequest pageRequest = checkValidation.checkPageSize(from, size);
        log.info("Получен список вещей подходящие под запрос поиска : {}", text);
        return itemRepository.search(text, pageRequest).stream()
                .map(item -> ItemMapper.toItemDto(item)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = UserMapper.toUser(userService.getUserByID(userId));
        itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Вещь с " + itemId + "  не найдена"));
        bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndEndBefore(itemId, userId, Status.APPROVED,
                LocalDateTime.now()).orElseThrow(() -> new ValidationException("Вы не можете оставить отзыв " +
                "на вешь с ID = " + itemId + ", так как вы не брали ее в аренду"));
        Comment comment = CommentMapper.toComment(commentDto, user, itemId);
        log.info("Пользователь с ID = {}, добавил коментарий {}, для вещи с ID ={}", userId, commentDto, itemId);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

}
