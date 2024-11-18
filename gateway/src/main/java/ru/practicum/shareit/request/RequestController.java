package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.request.dto.RequestDto;

@Controller
@RequestMapping("/requests")
@Slf4j
@RequiredArgsConstructor
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @Valid @RequestBody RequestDto requestDto) {
        log.info("Create request: {}", requestDto);
        return requestClient.createItemRequest(userId, requestDto);
    }

    @GetMapping("/all")
    ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") long requesterId) {
        log.info("Get all requests: {}", requesterId);
        return requestClient.findAll(requesterId);
    }

    @GetMapping
    ResponseEntity<Object> getRequesterList(@RequestHeader("X-Sharer-User-Id") long requesterId) {
        log.info("Get requests: {}", requesterId);
        return requestClient.findByRequester(requesterId);
    }

    @GetMapping("/{requestId}")
    ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable Long requestId) {
        log.info("Get request by id: {}", requestId);
        return requestClient.finById(userId, requestId);
    }
}
