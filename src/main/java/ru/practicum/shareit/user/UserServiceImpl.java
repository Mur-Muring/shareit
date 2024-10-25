package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers().stream().map(UserMapper::toDto).toList();
    }

    @Override
    public UserDto getUser(Long id) {
        return UserMapper.toDto(userStorage.getUser(id).orElseThrow(() -> new NotFoundException("User not found")));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.fromDto(userDto);
        return UserMapper.toDto(userStorage.createUser(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        User user = UserMapper.fromDto(userDto);
        return UserMapper.toDto(userStorage.updateUser(user, id));
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }
}
