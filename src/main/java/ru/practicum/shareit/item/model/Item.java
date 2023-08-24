package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Builder
public class Item {
    private long id;
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть не меньше 2 и не больше 30 символов")
    private String name;
    @NotNull(message = "Описание не может быть пустым")
    @Size(max = 500)
    private String description;
    @NotNull(message = "Статус не может быть пустой")
    private boolean available;
    private User owner;
    private ItemRequest request;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
