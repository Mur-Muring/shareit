package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateDataException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Get all users");
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::toDtoUser).toList();
    }

    @Override
    public UserDto getUser(Long id) {
        log.info("Get user by id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return UserMapper.toDtoUser(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Create user: {}", userDto);
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateDataException("Email already exists");
        }
        User user = UserMapper.toUser(userDto);
        return UserMapper.toDtoUser(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        log.info("Update user with id {}", userId);
        User updateUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        if (userDto.getEmail() != null && !updateUser.getEmail().equals(userDto.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new DuplicateDataException("Email already exists");
            }
            updateUser.setEmail(userDto.getEmail());
        }

        if (userDto.getName() != null) {
            updateUser.setName(userDto.getName());
        }

        return UserMapper.toDtoUser(userRepository.save(updateUser));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
