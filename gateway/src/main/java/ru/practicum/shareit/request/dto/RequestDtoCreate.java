package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDtoCreate {

    private Long id;
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    private Long requesterId;
}
