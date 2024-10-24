package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ItemDto {
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotBlank
    String available;
    Long request;
}