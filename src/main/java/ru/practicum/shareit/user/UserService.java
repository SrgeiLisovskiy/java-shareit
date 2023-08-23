package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User createUser(UserDto userDto);

    User updateUser(Long id, UserDto userDto);

    User getUserByID(Long id);

    List<User> getAllUsers();

    void deleteUserById(Long id);
}
