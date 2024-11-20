package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoCreateUpdate;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addComment(Long itemId, long userId, CommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }

    public ResponseEntity<Object> searchItem(String text) {
        return get("/search?text=" + text);
    }

    public ResponseEntity<Object> deleteItem(Long id) {
        return delete("/" + id);
    }

    public ResponseEntity<Object> getUserItem(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItem(Long id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> updateItem(ItemDtoCreateUpdate itemDto, Long userId, Long itemId) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> createItem(ItemDtoCreateUpdate itemDto, Long userId) {
        return post("", userId, itemDto);
    }
}