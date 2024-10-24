package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
@Slf4j
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items;


    @Override
    public Item getItem(Long id) {
        return items.get(id);
    }

    @Override
    public List<Item> getUserItem(Long id) {
        log.info("Getting user item with id {}", id);
        return items.values().stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), id))
                .collect(Collectors.toList());
    }

    @Override
    public Item createItem(Item item) {
        log.info("Creating new item {}", item);
        item.setId(getNextId());
        items.put(item.getId(), item);
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
                oldItem.setAvailable(Boolean.parseBoolean(itemDto.getAvailable()));
            }
            items.put(oldItem.getId(), oldItem);
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
        items.remove(id);
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

    @Override
    public Long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
