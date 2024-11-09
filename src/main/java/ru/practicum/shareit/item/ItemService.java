package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.NotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto getItem(Long id);

    List<ItemDto> getUserItem(long id);

    ItemDto createItem(ItemDto itemDto, Long id);

    ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) throws NotOwnerException;

    void deleteItem(Long id);

    List<ItemDto> searchItem(String searchString);
}