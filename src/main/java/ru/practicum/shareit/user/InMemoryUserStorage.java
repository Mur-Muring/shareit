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
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.get(id));

    }

    @Override
    public User createUser(User user) {
        for (User userNew : users.values()) {
            if (user.getEmail().equals(userNew.getEmail())) {
                throw new DuplicateDataException("This email is already in use");
            }
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user, Long id) {

        if (!users.containsKey(id)) {
            throw new NotFoundException("User not found");
        }
        User oldUser = users.get(id);

        if (user.getEmail() != null && !user.getEmail().equals(oldUser.getEmail())) {
            for (User existingUser : users.values()) {
                if (user.getEmail().equals(existingUser.getEmail())) {
                    throw new DuplicateDataException("This email is already in use");
                }
            }
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        return oldUser;
    }

    @Override
    public void deleteUser(Long id) {
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
