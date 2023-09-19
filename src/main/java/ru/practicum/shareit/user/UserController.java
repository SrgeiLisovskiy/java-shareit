package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;

import javax.transaction.Transactional;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
@Transactional
public class UserController {
    private final UserService userService;

    /**
     * Создание пользователя
     *
     * @param user Объект, содержащий данные для создания
     * @return Созданный пользователь
     */
    @PostMapping
    public UserDto createUser(@Validated({Create.class}) @RequestBody UserDto user) {
        log.debug("Получен запрос POST /users");
        return userService.createUser(user);
    }

    /**
     * Обновление пользователя
     *
     * @param userId ID объекта, содержащий данные для обновления
     * @param user   Объект, содержащий данные для обновления
     * @return Обновленный пользователь
     */
    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId,
                              @Validated({Update.class}) @RequestBody UserDto user) {
        log.debug("Получен запрос PATCH /users/{userId}");
        return userService.updateUser(userId, user);
    }

    /**
     * Получение пользователя по идентификатору
     *
     * @param userId Идентификатор пользователя
     * @return Пользователь
     */
    @GetMapping("/{userId}")
    public UserDto getUserByID(@PathVariable Long userId) {
        log.debug("Получен запрос GET /users/{id}");
        return userService.getUserByID(userId);
    }

    /**
     * Получение всех пользователей
     *
     * @return Список пользователей
     */
    @GetMapping
    public List<UserDto> getAllUsers() {
        log.debug("Получен запрос GET /users");
        return userService.getAllUsers();
    }

    /**
     * Удаление пользователя по идентификатору
     *
     * @param userId Идентификатор пользователя
     */
    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        log.debug("Получен запрос DEL /users/{userId}");
    }

}
