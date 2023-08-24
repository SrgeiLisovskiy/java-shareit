package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private Map<Long, User> users = new HashMap<>();
    private List<String> emails = new ArrayList<>();
    private long id;


    @Override
    public UserDto createUser(UserDto userDto) {
        for (String email : emails) {
            if (email.equals(userDto.getEmail())) {
                throw new ConflictException("Пользователь с таким email уже существует");
            }
        }
        emails.add(userDto.getEmail());
        id++;
        User user = UserMapper.toUser(userDto);
        user.setId(id);
        users.put(id, user);
        log.info("Пользователь добавлен: {}", user);
        return UserMapper.toUserDto(users.get(id));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (userDto.getEmail() != null) {
            if (!users.get(id).getEmail().equals(userDto.getEmail())) {
                for (String email : emails) {
                    if (email.equals(userDto.getEmail())) {
                        throw new ConflictException("Пользователь с таким email уже существует");
                    }
                }
                emails.remove(users.get(id).getEmail());
                users.get(id).setEmail(userDto.getEmail());
                emails.add(users.get(id).getEmail());
            }
        }
        if (userDto.getName() != null) {
            users.get(id).setName(userDto.getName());
        }
        log.info("Пользователь обновлен: {}", users.get(id));
        return UserMapper.toUserDto(users.get(id));
    }

    @Override
    public UserDto getUserByID(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Пользователь с ID = {} :", id);
        return UserMapper.toUserDto(users.get(id));

    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Получен список пользователей");
        return users.values().stream()
                .map(user -> UserMapper.toUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("");
        }
        emails.remove(users.get(id).getEmail());
        users.remove(id);
        log.info("Пользователь с ID = {} удален : ", id);
    }


}
