package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotOwnerException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto getItem(Long id) {
        log.info("Getting item by id: {}", id);
        Item item = getItemById(id);

        ItemDto dto = ItemMapper.toDto(item);

        dto.setLastBooking(getLastBookingEnd(id));
        dto.setNextBooking(getNextBookingStart(id));
        dto.setComments(getItemComments(id));

        return dto;
    }

    @Override
    public List<ItemDto> getUserItem(long id) {
        log.info("getUserItem: {}", id);
        return itemRepository.findByOwner_id(id).stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long id) {
        log.info("Creating new item: {}", itemDto);
        User owner = getUserById(id);
        Item item;
        if (itemDto.getRequestId() != null && itemDto.getRequestId() != 0) {
            Optional<ItemRequest> itemRequestOptional = itemRequestRepository.findById(itemDto.getRequestId());
            if (itemRequestOptional.isEmpty()) {
                throw new NotFoundException("Request not found");
            }
            item = ItemMapper.fromDtoWithRequest(itemDto, owner, itemRequestOptional.get());
        } else {
            item = ItemMapper.fromDto(itemDto, owner);
        }
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) throws NotOwnerException {
        log.info("Updating item: {}", itemDto);
        Item updateItem = getItemById(itemId);

        if (!updateItem.getOwner().getId().equals(userId)) {
            throw new NotOwnerException("User with id " + userId + " is not owner of this item");
        }

        if (itemDto.getName() != null) {
            updateItem.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            updateItem.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            updateItem.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toDto(itemRepository.save(updateItem));
    }

    @Override
    public void deleteItem(Long id) {
        log.info("Deleting item: {}", id);
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        log.info("Searching item: {}", text);
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text.toUpperCase()).stream()
                .filter(Item::isAvailable)
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public CommentDto addComment(Long itemId, long userId, CommentDto commentDto) {
        log.info("Add comment: {}", commentDto);
        User author = getUserById(userId);

        Item item = getItemById(itemId);

        boolean hasUserRentedItem = bookingRepository.existsByItemIdAndBookerIdAndEndIsBefore(itemId, userId, LocalDateTime.now());
        if (!hasUserRentedItem) {
            throw new ConditionsNotMetException("User with id " + userId + " is not rented yet");
        }

        Comment comment = CommentMapper.toComment(commentDto, item, author);

        return CommentMapper.toDtoComment(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getItemComments(Long itemId) {
        log.info("getItemComments: {}", itemId);
        return commentRepository.findByItem_Id(itemId).stream()
                .map(CommentMapper::toDtoComment)
                .toList();
    }

    private LocalDateTime getLastBookingEnd(Long itemId) {
        LocalDateTime localDateTime = LocalDateTime.now().withSecond(0).withNano(0);
        Booking lastBooking = bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(itemId, localDateTime)
                .orElse(null);
        return lastBooking != null ? lastBooking.getEnd() : null;
    }

    private LocalDateTime getNextBookingStart(Long itemId) {
        log.info("getNextBookingStart: {}", itemId);
        LocalDateTime localDateTime = LocalDateTime.now();
        return bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(itemId, localDateTime)
                .map(Booking::getStart)
                .orElse(null);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    private Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
    }
}
