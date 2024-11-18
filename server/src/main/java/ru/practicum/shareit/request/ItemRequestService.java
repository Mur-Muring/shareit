package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> findAll(long idRequester);

    List<ItemRequestDto> findByRequester(long idRequester);

    ItemRequestDto finById(long userId, long requestId);
}
