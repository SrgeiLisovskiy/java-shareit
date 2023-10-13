package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    private User user1;
    private User user2;

    @BeforeEach
    void beforeEach() {
        user1 = User.builder()
                .id(1L)
                .email("email@email.ru")
                .name("user1")
                .build();

        user2 = User.builder()
                .id(2L)
                .email("mail2@email.ru")
                .name("user2")
                .build();
    }

    @Test
    @DisplayName("Проверка createUser в UserServiceImpl")
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(user1);
        UserDto userDtoTest = userService.createUser(UserMapper.toUserDto(user1));
        assertEquals(1L, userDtoTest.getId());
        assertEquals("email@email.ru", userDtoTest.getEmail());
        assertEquals("user1", userDtoTest.getName());

        verify(userRepository, times(1)).save(user1);
    }

    @Test
    @DisplayName("Проверка updateUser вUserServiceImpl")
    void updateUser() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail(anyString())).thenReturn(List.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);
        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .email("update@mail.ru")
                .name("userUpdate")
                .build();

        UserDto userDtoUpdate = userService.updateUser(1L, userDto1);

        assertEquals(userDtoUpdate.getEmail(), userDto1.getEmail());
        assertEquals(userDtoUpdate.getName(), userDto1.getName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(UserMapper.toUser(userDto1));


    }

    @Test
    void updateUserNotById() {
        when(userRepository.findById(anyLong())).thenThrow(new NotFoundException("Пользователь  не найден"));
        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .email("update@mail.ru")
                .name("userUpdate")
                .build();

        final NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> userService.updateUser(5L, userDto1));

        assertEquals("Пользователь  не найден", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка getUserByID в UserServiceImpl")
    void getUserByID() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        UserDto userDtoTest = userService.getUserByID(1L);

        assertEquals(1L, userDtoTest.getId());
        assertEquals("email@email.ru", userDtoTest.getEmail());
        assertEquals("user1", userDtoTest.getName());

        verify(userRepository, times(1)).findById(user1.getId());
    }

    @Test
    @DisplayName("Проверка getAllUsers в UserServiceImpl")
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        UserDto userDto1 = UserMapper.toUserDto(user1);
        UserDto userDto2 = UserMapper.toUserDto(user2);

        List<UserDto> userDtoList = userService.getAllUsers();

        assertEquals(userDtoList, List.of(userDto1, userDto2));

        verify(userRepository, times(1)).findAll();

    }

    @Test
    @DisplayName("Проверка deleteUserById в UserServiceImpl")
    void deleteUserById() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Проверка на добовление нового пользователя с существующим email")
    void checkUserEmailForDuplicateTest() {
        when(userRepository.save(any(User.class))).thenThrow(new ConflictException("Пользователь с "
                + user1.getEmail() + " уже существует"));

        final ConflictException exception = Assertions.assertThrows(ConflictException.class,
                () -> userService.createUser(UserMapper.toUserDto(user1)));

        assertEquals("Пользователь с " + user1.getEmail() + " уже существует", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка на несуществующего пользователя")
    void checkNotExistsUserByIdTest() {
        when(userRepository.findById(anyLong())).thenThrow(new NotFoundException("Пользователь с ID = 2 не найден"));

        final NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> userService.getUserByID(5L));

        assertEquals("Пользователь с ID = 2 не найден", exception.getMessage());
    }
}