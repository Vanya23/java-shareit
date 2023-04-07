package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ItemRequestDtoIn {
    @NotBlank(groups = {Create.class})
    String description; // текст запроса, содержащий описание требуемой вещи;

}
