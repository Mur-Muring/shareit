package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto createItemRequest(long userId, ItemRequestDto itemRequestDto) {
        log.info("Create item request: {}", itemRequestDto);

        User requester = getUserById(userId);

        ItemRequest itemRequest = ItemRequest.builder()
                .requester(requester)
                .created(LocalDateTime.now())
                .description(itemRequestDto.getDescription())
                .build();

        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> findAll(long idRequester) {
        log.info("Find all item requests: {}", idRequester);

        List<ItemRequest> itemRequestList = itemRequestRepository.findAll(Sort.by(Sort.Direction.DESC, "created"));
        return itemRequestList.stream()
                .filter(ItemRequest -> ItemRequest.getRequester().getId() != idRequester)
                .map(ItemRequestMapper::toDto)
                .toList();
    }

    @Override
    public List<ItemRequestDto> findByRequester(long requesterId) {
        log.info("Find all item by requester: {}", requesterId);

        getUserById(requesterId);

        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdOrderByCreatedDesc(requesterId);

        return requests.stream().map(ItemRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto finById(long userId, long requestId) {
        log.info("Find item request by id: {}", requestId);
        getUserById(userId);
        Optional<ItemRequest> itemRequestOptional = itemRequestRepository.findById(requestId);
        if (itemRequestOptional.isEmpty()) {
            throw new NotFoundException("Request not found");
        }
        List<ItemDtoShort> item = itemRepository.findByRequest_Id(requestId).stream()
                .map(ItemMapper::toDtoShort)
                .toList();
        ItemRequestDto itemRequestDto = ItemRequestMapper.toDto(itemRequestOptional.get());
        itemRequestDto.setItems(item);
        return itemRequestDto;
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }
}
