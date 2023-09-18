package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));

    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = UserMapper.toUser(getUserByID(id));
        if (user == null) {
            throw new NotFoundException("Пользователь с" + id + " не найден");
        }
        if (userDto.getEmail() != null) {
            if (!user.getEmail().equals(userDto.getEmail())) {
                checkUserEmailForDuplicate(userDto.getEmail());
                user.setEmail(userDto.getEmail());
            }
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        userRepository.save(user);
        return UserMapper.toUserDto(UserMapper.toUser(getUserByID(id)));
    }

    @Override
    public UserDto getUserByID(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с " + id + "  не найден"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    private void checkUserEmailForDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Пользователь с " + email + " уже существует");
        }

    }
}
