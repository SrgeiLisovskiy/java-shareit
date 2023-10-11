package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.utilite.Create;
import ru.practicum.shareit.utilite.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDto {
    @NotNull(groups = {Update.class}, message = "ID не может быть пустым")
    private long id;
    @Email(groups = {Create.class, Update.class}, message = "Неверный Email")
    @NotBlank(groups = {Create.class}, message = "Email не может быть пустым")
    private String email;
    @Size(groups = {Create.class, Update.class}, max = 50, message = "Имя должно быть не меньше 2 и не больше 30 символов")
    @NotBlank(groups = {Create.class}, message = "Имя не может быть пустым")
    private String name;
}
