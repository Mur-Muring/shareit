package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.DuplicateDataException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
@AllArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users;

    @Override
    public List<User> getAllUsers() {
        log.info("Get all users");
        return users.values().stream().toList();
    }

    @Override
    public User getUser(Long id) {
        log.info("Get user {}", id);
        return Optional.ofNullable(users.get(id)).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User createUser(User user) {
        log.info("Create user {}", user);
        for (User user1 : users.values()) {
            if (user.getEmail().equals(user1.getEmail())) {
                throw new DuplicateDataException("This email is already in use");
            }
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user, Long id) {
        log.info("Update user {}", user);
        if (users.containsKey(id)) {
            if (user.getEmail() != null) {
                for (User user1 : users.values()) {
                    if (user.getEmail().equals(user1.getEmail())) {
                        if (!user1.getId().equals(id)) {
                            throw new DuplicateDataException("This email is already in use");
                        }
                    }
                }
            }
            User oldUser = users.get(id);
            if (user.getName() != null) {
                oldUser.setName(user.getName());
            }
            if (user.getEmail() != null) {
                oldUser.setEmail(user.getEmail());
            }
            return oldUser;
        }
        throw new NotFoundException("User with id = " + id + " not found");
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Delete user {}", id);
        if (!users.containsKey(id)) {
            throw new NotFoundException("User with id = " + id + " not found");
        }
        users.remove(id);
    }

    @Override
    public Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
