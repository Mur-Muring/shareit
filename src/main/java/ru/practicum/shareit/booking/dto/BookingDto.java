package ru.practicum.shareit.booking.dto;

/**
 * TODO Sprint add-bookings.
 */

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    LocalDateTime startTime;
    LocalDateTime endTime;
    Item item;
    User booker;
    BookingStatus status;
}
