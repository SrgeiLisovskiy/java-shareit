package ru.practicum.shareit.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {
    User user = User.builder()
            .id(1L)
            .email("email@email.ru")
            .name("user1")
            .build();
    UserDto userDto = UserDto.builder()
            .id(1L)
            .name("userDto")
            .email("userDto@mail.ru")
            .build();

    @Test
    @DisplayName("Проверка toUserDto в UserMapper")
    void toUserDto() {
        UserDto userDtoResult = UserMapper.toUserDto(user);

        assertEquals(userDtoResult.getId(), user.getId());
        assertEquals(userDtoResult.getName(), user.getName());
        assertEquals(userDtoResult.getEmail(), user.getEmail());
    }

    @Test
    @DisplayName("Проверка toUser в UserMapper")
    void toUser() {
        User userResult = UserMapper.toUser(userDto);

        assertEquals(userDto.getId(), userResult.getId());
        assertEquals(userDto.getName(), userResult.getName());
        assertEquals(userDto.getEmail(), userResult.getEmail());
    }
}