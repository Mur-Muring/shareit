package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@AllArgsConstructor
@Slf4j
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items;
    private final Map<Long, List<Item>> itemsList;


    @Override
    public Item getItem(Long id) {
        return items.get(id);
    }

    @Override
    public List<Item> getUserItem(Long id) {
        log.info("Getting user item with id {}", id);
        return itemsList.getOrDefault(id, new ArrayList<>());
    }

    @Override
    public Item createItem(Item item) {
        log.info("Creating new item {}", item);
        Long itemId = getNextId();
        item.setId(itemId);
        items.put(itemId, item);
        itemsList.computeIfAbsent(item.getOwner().getId(), k -> new ArrayList<>()).add(item);
        return item;
    }

    @Override
    public Item updateItem(ItemDto itemDto, Long itemId) {
        log.info("Updating item with id {}", itemId);
        if (items.containsKey(itemId)) {
            Item oldItem = items.get(itemId);
            if (itemDto.getName() != null) {
                oldItem.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                oldItem.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                oldItem.setAvailable(itemDto.getAvailable());
            }
            items.put(oldItem.getId(), oldItem);
            List<Item> itemNewList = itemsList.get(oldItem.getOwner().getId());
            itemNewList.add(oldItem);

            return oldItem;
        }
        throw new NotFoundException("Item not found");
    }

    @Override
    public void deleteItem(Long id) {
        log.info("Deleting item with id {}", id);
        if (!items.containsKey(id)) {
            throw new NotFoundException("Item not found");
        }
        Item item = items.remove(id);
        List<Item> itemNewList = itemsList.get(item.getOwner().getId());
        if (itemNewList != null) {
            itemNewList.removeIf(i -> i.getId().equals(id));
            if (itemNewList.isEmpty()) {
                itemsList.remove(item.getOwner().getId());
            }
        }
    }

    @Override
    public List<Item> searchItem(String searchString) {
        log.info("Searching for items with {}", searchString);
        return items.values().stream()
                .filter(item -> item.getName().equalsIgnoreCase(searchString) ||
                        item.getDescription().equalsIgnoreCase(searchString))
                .filter(Item::isAvailable)
                .toList();
    }

    private Long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}