package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    User getUser(Long id);

    User createUser(User user);

    User updateUser(User user, Long id);

    void deleteUser(Long id);

    Long getNextId();
}
