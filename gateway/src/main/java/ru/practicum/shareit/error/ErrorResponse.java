package ru.practicum.shareit.error;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private int status;
    private String error;

    private String description;

    public ErrorResponse(int status, String error, String description) {
        this.status = status;
        this.error = error;
        this.description = description;
    }

    public ErrorResponse(String error) {
        this.error = error;
    }

}

