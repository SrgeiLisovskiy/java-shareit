package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private final UserService userService;

    /**
     * Создание пользователя
     *
     * @param user Объект, содержащий данные для создания
     * @return Созданный пользователь
     */
    @PostMapping
    public User createUser(@Validated({Create.class}) @RequestBody UserDto user, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Проверьте введенные поля");
        }
        log.debug("Получен запрос POST /users");
        return userService.createUser(user);
    }

    /**
     * Обновление пользователя
     *
     * @param userId ID объекта, содержащий данные для обновления
     * @param user Объект, содержащий данные для обновления
     * @return Обновленный пользователь
     */
    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable Long userId,
                           @Validated({Update.class}) @RequestBody UserDto user,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new ValidationException("Проверьте введенные поля");
        }
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
    public User getUserByID(@PathVariable Long userId) {
        log.debug("Получен запрос GET /users/{id}");
        return userService.getUserByID(userId);
    }

    /**
     * Получение всех пользователей
     *
     * @return Список пользователей
     */
    @GetMapping
    public List<User> getAllUsers() {
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
