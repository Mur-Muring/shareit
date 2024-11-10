package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.NotOwnerException;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(BookingDto bookingDto, Long userId);

    BookingDto approveBooking(Long userId, Long bookingId, Boolean approved) throws NotOwnerException;

    BookingDto getById(Long userId, Long id) throws NotOwnerException;

    List<BookingDto> getAllBookingsByBooker(Long bookerId, String state);

    List<BookingDto> getAllBookingsByOwner(Long ownerId, String state);
}
