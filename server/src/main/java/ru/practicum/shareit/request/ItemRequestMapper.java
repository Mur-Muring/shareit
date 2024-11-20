package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

public class ItemRequestMapper {
    public static ItemRequestDto toDto(ItemRequest itemRequest) {
        return ItemRequestDto
                .builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }
}
