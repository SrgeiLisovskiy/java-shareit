package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.utilite.Create;
import ru.practicum.shareit.utilite.Update;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    /**
     * Создание пользователя
     *
     * @param user Объект, содержащий данные для создания
     * @return Созданный пользователь
     */
    @PostMapping
    public ResponseEntity<Object> createUser(@Validated({Create.class}) @RequestBody UserDto user) {
        log.debug("Получен запрос POST /users");
        return userClient.createUser(user);
    }

    /**
     * Обновление пользователя
     *
     * @param userId ID объекта, содержащий данные для обновления
     * @param user   Объект, содержащий данные для обновления
     * @return Обновленный пользователь
     */
    @PatchMapping("/{userId}")
    public  ResponseEntity<Object> updateUser(@PathVariable Long userId,
                              @Validated({Update.class}) @RequestBody UserDto user) {
        log.debug("Получен запрос PATCH /users/{userId}");
        return userClient.updateUser(userId, user);
    }

    /**
     * Получение пользователя по идентификатору
     *
     * @param userId Идентификатор пользователя
     * @return Пользователь
     */
    @GetMapping("/{userId}")
    public  ResponseEntity<Object> getUserByID(@PathVariable Long userId) {
        log.debug("Получен запрос GET /users/{id}");
        return userClient.getUserByID(userId);
    }

    /**
     * Получение всех пользователей
     *
     * @return Список пользователей
     */
    @GetMapping
    public  ResponseEntity<Object> getAllUsers() {
        log.debug("Получен запрос GET /users");
        return userClient.getAllUsers();
    }

    /**
     * Удаление пользователя по идентификатору
     *
     * @param userId Идентификатор пользователя
     */
    @DeleteMapping("/{userId}")
    public  ResponseEntity<Object> deleteUserById(@PathVariable Long userId) {
        log.debug("Получен запрос DEL /users/{userId}");
        return  userClient.deleteUserById(userId);
    }

}
