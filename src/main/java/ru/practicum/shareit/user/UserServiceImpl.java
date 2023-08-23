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

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private Map<Long, User> users = new HashMap<>();
    private List<String> emails = new ArrayList<>();
    private long id;


    @Override
    public User createUser(UserDto userDto) {
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
        return users.get(id);
    }

    @Override
    public User updateUser(Long id, UserDto userDto) {
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
        return users.get(id);
    }

    @Override
    public User getUserByID(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Пользователь с ID = {} :", id);
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получен список пользователей");
        return new ArrayList<>(users.values());
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
