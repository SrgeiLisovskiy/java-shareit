package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {
    public static ItemRequest toItemRequest(User user, ItemRequestDto itemRequestDto){
       return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .requester(user)
                .description(itemRequestDto.getDescription())
                .created(LocalDateTime.now())
                .build();
    }
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest){
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(new ArrayList<>())
                .build();
    }
}
