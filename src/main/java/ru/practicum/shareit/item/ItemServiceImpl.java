package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public ItemDto getItem(Long id) {
        return ItemMapper.toDto(itemStorage.getItem(id));
    }

    @Override
    public List<ItemDto> getUserItem(long id) {
        userService.getUser(id);
        return itemStorage.getUserItem(id).stream().map(ItemMapper::toDto).toList();
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long id) {
        User user = UserMapper.fromDto(userService.getUser(id));
        user.setId(id);
        Item item = ItemMapper.fromDto(itemDto, user);
        return ItemMapper.toDto(itemStorage.createItem(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) throws NotOwnerException {
        if (!Objects.equals(itemStorage.getItem(itemId).getOwner().getId(), userId)) {
            throw new NotOwnerException("Not owner of this item");
        }
        return ItemMapper.toDto(itemStorage.updateItem(itemDto, itemId));
    }

    @Override
    public void deleteItem(Long id) {
        itemStorage.deleteItem(id);
    }

    @Override
    public List<ItemDto> searchItem(String searchString) {
        if (searchString == null || searchString.isEmpty()) {
            return Collections.emptyList();
        }
        return itemStorage.searchItem(searchString).stream().map(ItemMapper::toDto).toList();
    }
}

