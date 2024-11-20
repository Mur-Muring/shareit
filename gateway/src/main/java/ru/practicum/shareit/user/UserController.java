package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        log.info("Get user with id {}", id);
        return userClient.getUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get all users");
        return userClient.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto user) {
        log.info("Create user {}", user);
        return userClient.createUser(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto user, @PathVariable Long id) {
        log.info("Update user {} with id {}", user, id);
        return userClient.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        log.info("Delete user with id {}", id);
        return userClient.deleteUser(id);
    }
}
