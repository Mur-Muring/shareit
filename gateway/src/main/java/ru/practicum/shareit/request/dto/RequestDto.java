package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private Long id;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    private Long requesterId;
    private LocalDateTime created;
    private List<ItemDto> items;
}
