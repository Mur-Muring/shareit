package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotOwnerException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDto addBooking(BookingDto bookingDto, Long userId) {
        log.info("Add booking: {}", bookingDto);
        User user = getUserById(userId);

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item with id " + bookingDto.getItemId() + " not found"));

        if (!item.isAvailable()) {
            throw new ConditionsNotMetException("Item with id " + bookingDto.getItemId() + " is not available");
        }

        boolean hasOverlap = bookingRepository.existsByItemIdAndEndAfterAndStartBefore(item.getId(), bookingDto.getStart(), bookingDto.getEnd());
        if (hasOverlap) {
            throw new ConditionsNotMetException("Reservation already exists");
        }

        Booking booking = BookingMapper.toBooking(bookingDto, item, user);
        booking.setStatus(BookingStatus.WAITING);

        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(savedBooking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long userId, Long bookingId, Boolean approved) throws NotOwnerException {
        log.info("approveBooking: bookingId={}, userId={}", bookingId, userId);
        Booking booking = bookingRepository.findByIdAndItemOwnerId(bookingId, userId)
                .orElseThrow(() -> new NotOwnerException("User is not owner of item"));

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ConditionsNotMetException("Reservation already exists");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        Booking updatedBooking = bookingRepository.save(booking);

        return BookingMapper.toBookingDto(updatedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getById(Long userId, Long id) throws NotOwnerException {
        log.info("getById: userId={}, id={}", userId, id);
        Booking booking = getBookingById(id);

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotOwnerException("User is not owner of item");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookingsByBooker(Long bookerId, String state) {
        log.info("getAllBookingsByBooker bookerId={}, state={}", bookerId, state);
        getUserById(bookerId);

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();
        bookings = switch (state.toUpperCase()) {
            case "CURRENT" ->
                    bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId, now, now);
            case "PAST" -> bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(bookerId, now);
            case "FUTURE" -> bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(bookerId, now);
            case "WAITING" ->
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
            case "REJECTED" ->
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
            default -> bookingRepository.findByBookerIdOrderByStartDesc(bookerId);
        };
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookingsByOwner(Long ownerId, String state) {
        log.info("getAllBookingsByOwner ownerId={}, state={}", ownerId, state);
        getUserById(ownerId);

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();
        bookings = switch (state.toUpperCase()) {
            case "CURRENT" ->
                    bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, now, now);
            case "PAST" -> bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, now);
            case "FUTURE" -> bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, now);
            case "WAITING" ->
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
            case "REJECTED" ->
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
            default -> bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);
        };
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    @Transactional(readOnly = true)
    private Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Reservation with id " + bookingId + " not found"));
    }
}
