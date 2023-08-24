package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class User {
    private long id;
    @NotNull(message = "Email не может быть пустым")
    @Email(message = "Неверный Email")
    private String email;
    @Size(max = 30, message = "Имя должно быть не меньше 2 и не больше 30 символов")
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
}
