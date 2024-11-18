package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping("/all")
    List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") long requesterId) {
        return itemRequestService.findAll(requesterId);
    }

    @GetMapping
    List<ItemRequestDto> getRequesterList(@RequestHeader("X-Sharer-User-Id") long requesterId) {
        return itemRequestService.findByRequester(requesterId);
    }

    @GetMapping("/{requestId}")
    ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable Long requestId) {
        return itemRequestService.finById(userId, requestId);
    }
}
