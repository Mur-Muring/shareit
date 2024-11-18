package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item getItem(Long id);

    List<Item> getUserItem(Long id);

    Item createItem(Item item);

    Item updateItem(ItemDto itemDto, Long itemId);

    void deleteItem(Long id);

    List<Item> searchItem(String searchString);

    Long getNextId();
}
