package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    @NotNull(groups = {Update.class})
    private long id;
    @NotBlank(groups = {Create.class}, message = "Имя не может быть пустым" )
    @Size(groups = {Create.class}, min = 2,max = 30, message = "Имя должно быть не меньше 2 и не больше 30 символов" )
    private String name;
    @NotBlank(groups = {Create.class}, message = "Описание не может быть пустым")
    @Size(groups = {Create.class}, max = 500)
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private long request;
}
