package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Controller
@RequestMapping("/items")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    ResponseEntity<Object> createItem(@RequestBody @Valid ItemDto itemDto,
                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Create new item: {}", itemDto);
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto,
                                      @RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable Long itemId) {
        log.info("Update item: {}", itemDto);
        return itemClient.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("/{id}")
    ResponseEntity<Object> getItem(@PathVariable Long id) {
        log.info("Get item: {}", id);
        return itemClient.getItem(id);
    }

    @GetMapping
    ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get user items: {}", userId);
        return itemClient.getUserItem(userId);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteItem(@PathVariable Long id) {
        log.info("Delete item: {}", id);
        return itemClient.deleteItem(id);
    }

    @GetMapping("/search")
    ResponseEntity<Object> searchItems(@RequestParam String text) {
        log.info("Search items: {}", text);
        return itemClient.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    ResponseEntity<Object> postComment(@PathVariable Long itemId,
                                       @Valid @RequestBody CommentDto commentDto,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Comment item: {}", itemId);
        return itemClient.addComment(itemId, userId, commentDto);
    }
}
