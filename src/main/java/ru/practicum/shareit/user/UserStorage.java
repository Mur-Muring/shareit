package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.DuplicateDataException;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    User getUser(Long id);

    User createUser(User user) throws DuplicateDataException;

    User updateUser(User user, Long id);

    void deleteUser(Long id);

    Long getNextId();
}
